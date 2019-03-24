package com.ruchij.dao

import com.ruchij.FutureOpt
import com.ruchij.dao.ReactiveMongoUrlDao.MongoDatabase
import com.ruchij.exceptions.{DatabaseException, EmptyOptionException}
import com.ruchij.monad.FoldableMonad.TryMonad.predicate
import com.ruchij.services.url.models.Url
import javax.inject.Inject
import play.api.libs.json._
import reactivemongo.api.{Cursor, DefaultDB}
import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter
import reactivemongo.play.json.collection.JSONBatchCommands.FindAndModifyCommand.FindAndModifyResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future.fromTry
import scala.concurrent.{ExecutionContext, Future}

class ReactiveMongoUrlDao @Inject()(mongoDatabase: MongoDatabase) extends UrlDao {
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
          _.findAndUpdate(Json.obj("key" -> key), Json.obj("$inc" -> Json.obj("hits" -> 1)), fetchNewObject = true)
        }
        .map(_.result[Url])
    }
  override def fetchAll(page: Int, pageSize: Int)(implicit executionContext: ExecutionContext): Future[List[Url]] =
    urls.flatMap {
      _.find(Json.obj(), Option.empty[JsObject])
        .sort(Json.obj("createdAt" -> -1))
        .skip(page * pageSize)
        .cursor[Url]()
        .collect(pageSize, Cursor.FailOnError[List[Url]]())
    }
  override def delete(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key)
      .flatMapM {
        url => urls.flatMap[FindAndModifyResult](_.findAndRemove(Json.toJsObject(url))).map(_ => url)
      }
}

object ReactiveMongoUrlDao {
  case class MongoDatabase(database: Future[DefaultDB], collectionName: String = "urls")
}
