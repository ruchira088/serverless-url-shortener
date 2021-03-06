package com.ruchij.dao

import java.sql.Timestamp

import com.ruchij.FutureOpt
import com.ruchij.dao.models.InitializationResult
import com.ruchij.dao.models.InitializationResult.SlickDaoInitializationResult
import com.ruchij.exceptions.{DatabaseException, EmptyOptionException}
import com.ruchij.monad.Monad.futureMonad
import com.ruchij.services.url.models.Url
import org.joda.time.DateTime
import slick.ast.BaseTypedType
import slick.basic.{BasicBackend, DatabaseConfig}
import slick.jdbc.{JdbcProfile, JdbcType, SQLiteProfile}
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class SlickUrlDao(val jdbcProfile: JdbcProfile, db: BasicBackend#DatabaseDef) extends UrlDao {

  import jdbcProfile.api._

  implicit val dateTimeMapper: JdbcType[DateTime] with BaseTypedType[DateTime] =
    MappedColumnType.base[DateTime, Timestamp](
      dateTime => new Timestamp(dateTime.getMillis),
      timestamp => new DateTime(timestamp.getTime)
    )

  class Urls(tag: Tag) extends Table[Url](tag, SlickUrlDao.TABLE_NAME) {

    def key: Rep[String] = column[String]("url_key", O.PrimaryKey)

    def createdAt: Rep[DateTime] = column[DateTime]("created_at")

    def longUrl: Rep[String] = column[String]("long_url")

    def hits: Rep[Double] = column[Double]("hits")

    override def * : ProvenShape[Url] =
      (key, createdAt, longUrl, hits) <> (Url.apply _ tupled, Url.unapply)
  }

  val urls = TableQuery[Urls]

  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] =
    for {
      _ <- db.run(urls += url)
      insertedUrl <- fetch(url.key).flatten
        .recoverWith {
          case EmptyOptionException => Future.failed(DatabaseException("Unable to fetch persisted item"))
        }
    } yield insertedUrl

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FutureOpt {
      db.run(urls.filter(_.key === key).result).map(_.headOption)
    }

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key)
      .flatMapM { url =>
        db.run(urls.filter(_.key === key).map(_.hits).update(url.hits + 1))
      }
      .flatMap { _ =>
        fetch(key)
      }

  override def fetchAll(page: Int, pageSize: Int)(implicit executionContext: ExecutionContext): Future[List[Url]] =
    db.run(urls.drop(page * pageSize).take(pageSize).sortBy(_.createdAt).result).map(_.toList)

  //
  // db.run(MTable.getTables(SlickUrlDao.TABLE_NAME)) is NOT supported by Aurora MYSQL Serverless
  //
  override def initialize()(implicit executionContext: ExecutionContext): FutureOpt[InitializationResult] =
    FutureOpt[InitializationResult, Future, Option] {
      jdbcProfile match {
        case SQLiteProfile => Future.successful(None)
        case _ =>
          db.run(sql"SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ${SlickUrlDao.TABLE_NAME}".as[Int])
            .flatMap {
              case Vector(0) =>
                db.run(urls.schema.create).map(_ => Some(SlickDaoInitializationResult(SlickUrlDao.TABLE_NAME)))

              case _ => Future.successful(None)
            }
      }
    }
}

object SlickUrlDao {
  val TABLE_NAME = "url"

  def apply(): SlickUrlDao = {
    val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("auroraServerless")
    new SlickUrlDao(databaseConfig.profile, databaseConfig.db)
  }
}
