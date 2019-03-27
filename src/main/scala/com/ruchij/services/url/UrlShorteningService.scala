package com.ruchij.services.url
import com.ruchij.FutureOpt
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.UrlDao
import com.ruchij.dao.models.InitializationResult
import com.ruchij.exceptions.{ExistingUrlKeyException, MissingUrlKeyException}
import com.ruchij.services.hashing.HashingService
import com.ruchij.services.url.UrlShorteningService.mapper
import com.ruchij.services.url.models.Url
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UrlShorteningService @Inject()(
  urlDao: UrlDao,
  hashingService: HashingService,
  val serviceConfiguration: ServiceConfiguration
) {
  def fetch(key: String)(implicit executionContext: ExecutionContext): Future[Url] =
    urlDao.incrementHit(key).value.flatMap(mapper(key))

  def info(key: String)(implicit executionContext: ExecutionContext): Future[Url] =
    urlDao.fetch(key).value.flatMap(mapper(key))

  def insert(longUrl: String)(implicit executionContext: ExecutionContext): Future[Either[Url, Url]] =
    insert(longUrl, Constants.EMPTY_STRING, serviceConfiguration.keyLength, serviceConfiguration.fixedKeyLengthRetries)

  def insert(key: String, longUrl: String)(implicit executionContext: ExecutionContext): Future[Either[Url, Url]] =
    urlDao.fetch(key).value
      .flatMap {
        case None =>
          urlDao.insert(Url(key, DateTime.now(), longUrl, 0)).map(Right.apply)

        case Some(url) =>
          if (url.longUrl == longUrl) Future.successful(Left(url)) else Future.failed(ExistingUrlKeyException(key))
      }

  def fetchAll(page: Int, pageSize: Int)(implicit executionContext: ExecutionContext): Future[List[Url]] =
    urlDao.fetchAll(page, pageSize)

  def initialize()(implicit executionContext: ExecutionContext): FutureOpt[InitializationResult] =
    urlDao.initialize()

  private def insert(longUrl: String, suffix: String, keySize: Int, remaining: Int)(
    implicit executionContext: ExecutionContext
  ): Future[Either[Url, Url]] =
    for {
      key <- hashingService.hashWithFixedLength(longUrl + suffix, keySize)
      existingUrl <- urlDao.fetch(key).value

      result <- existingUrl
        .fold[Future[Either[Url, Url]]](urlDao.insert(Url(key, DateTime.now(), longUrl, 0)).map(Right.apply)) { url =>
          (url.longUrl, remaining) match {
            case (`longUrl`, _) => Future.successful(Left(url))
            case (_, 0) => insert(longUrl, suffix + key, keySize + 1, serviceConfiguration.fixedKeyLengthRetries)
            case _ => insert(longUrl, suffix + key, keySize, remaining - 1)
          }
        }
    } yield result
}

object UrlShorteningService {
  def mapper(key: String): PartialFunction[Option[Url], Future[Url]] = {
    case Some(url) => Future.successful(url)
    case _ => Future.failed(MissingUrlKeyException(key))
  }
}
