package com.ruchij.services.url
import com.ruchij.dao.UrlDao
import com.ruchij.general.Constants
import com.ruchij.services.hashing.HashingService
import com.ruchij.services.url.models.Url
import exceptions.UrlKeyNotFoundException
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

class UrlShorteningService(urlDao: UrlDao, hashingService: HashingService) {
  def fetch(key: String)(implicit executionContext: ExecutionContext): Future[Url] =
    urlDao.fetch(key).future
      .flatMap {
        case Some(_) => urlDao.incrementHit(key)
        case _ => Future.failed(UrlKeyNotFoundException(key))
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
