package com.ruchij.services.url

import com.ruchij.dao.UrlDao
import com.ruchij.exceptions.MissingUrlKeyException
import com.ruchij.general.Constants
import com.ruchij.services.hashing.HashingService
import com.ruchij.services.url.models.Url
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UrlShorteningService @Inject()(urlDao: UrlDao, hashingService: HashingService) {
  def fetch(key: String)(implicit executionContext: ExecutionContext): Future[Url] =
    urlDao.incrementHit(key).future
      .flatMap {
        case Some(url) => Future.successful(url)
        case _ => Future.failed(MissingUrlKeyException(key))
      }

  def insert(longUrl: String, suffix: String = Constants.EMPTY_STRING)(
    implicit executionContext: ExecutionContext
  ): Future[Either[Url, Url]] =
    for {
      key <- hashingService.hash(longUrl + suffix)
      existingUrl <- urlDao.fetch(key).future

      result <- existingUrl
        .fold[Future[Either[Url, Url]]](urlDao.insert(Url(key, DateTime.now(), longUrl, 0)).map(Right.apply)) { url =>
          if (url.longUrl == longUrl) Future.successful(Left(url)) else insert(longUrl, suffix + key)
        }
    } yield result
}
