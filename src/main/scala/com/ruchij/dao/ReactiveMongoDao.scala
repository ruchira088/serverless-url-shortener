package com.ruchij.dao

import com.ruchij.FutureOpt
import com.ruchij.dao.ReactiveMongoDao.MongoDatabase
import com.ruchij.exceptions.{DatabaseException, EmptyOptionException}
import com.ruchij.monad.FoldableMonad.TryMonad.predicate
import com.ruchij.services.url.models.Url
import javax.inject.Inject
import play.api.libs.json._
import reactivemongo.api.DefaultDB
import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter
import reactivemongo.play.json.collection.JSONBatchCommands.FindAndModifyCommand.FindAndModifyResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future.fromTry
import scala.concurrent.{ExecutionContext, Future}

class ReactiveMongoDao @Inject()(mongoDatabase: MongoDatabase) extends UrlDao {
  def urls(implicit executionContext: ExecutionContext): Future[JSONCollection] =
    mongoDatabase.database.map(_.collection[JSONCollection](mongoDatabase.collectionName))

  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] =
    for {
      collection <- urls
      writeResult <- collection.insert.one(url)
      _ <- fromTry(predicate[DatabaseException](writeResult.ok, writeResult.writeErrors.mkString(", ")))
      fetchedUrl <- fetch(url.key).flatten.recoverWith {
        case EmptyOptionException => Future.failed(DatabaseException(s"Unable to fetch persisted URL: $url"))
      }
    } yield fetchedUrl

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FutureOpt {
      urls
        .flatMap {
          _.find(Json.obj("key" -> key), Option.empty[JsObject]).one[Url]
        }
    }

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FutureOpt {
      urls
        .flatMap[FindAndModifyResult] {
          _.findAndUpdate(
            Json.obj("key" -> key),
            Json.obj("$inc" -> Json.obj("hits" -> 1)),
            fetchNewObject = true
          )
        }
        .map(_.result[Url])
    }
}

object ReactiveMongoDao {
  case class MongoDatabase(database: Future[DefaultDB], collectionName: String = "urls")
}
