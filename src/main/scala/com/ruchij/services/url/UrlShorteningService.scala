package com.ruchij.services.url
import java.util.UUID

import com.ruchij.FutureOpt
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.UrlDao
import com.ruchij.dao.models.InitializationResult
import com.ruchij.exceptions.{ExistingUrlKeyException, IncorrectDeleteSecretException, MissingUrlKeyException}
import com.ruchij.general.Messages
import com.ruchij.providers.Providers
import com.ruchij.services.hashing.HashingService
import com.ruchij.services.url.UrlShorteningService.mapper
import com.ruchij.services.url.models.{ReadOnlyUrl, Url}
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UrlShorteningService @Inject()(urlDao: UrlDao, hashingService: HashingService)(
  implicit val serviceConfiguration: ServiceConfiguration,
  providers: Providers
) {
  def fetch(key: String)(implicit executionContext: ExecutionContext): Future[ReadOnlyUrl] =
    urlDao.incrementHit(key).value.flatMap(mapper(key))

  def info(key: String)(implicit executionContext: ExecutionContext): Future[ReadOnlyUrl] =
    urlDao.fetch(key).value.flatMap(mapper(key))

  def insert(longUrl: String)(implicit executionContext: ExecutionContext): Future[Either[ReadOnlyUrl, Url]] =
    insert(longUrl, Messages.EMPTY_STRING, serviceConfiguration.keyLength, serviceConfiguration.fixedKeyLengthRetries)

  def insert(key: String, longUrl: String)(
    implicit executionContext: ExecutionContext
  ): Future[Either[ReadOnlyUrl, Url]] =
    urlDao
      .fetch(key)
      .value
      .flatMap {
        case None =>
          urlDao.insert(Url(key, providers.dateTime(), longUrl, 0, providers.uuid())).map(Right.apply)

        case Some(url) =>
          if (url.longUrl == longUrl) Future.successful(Left(ReadOnlyUrl.fromUrl(url)))
          else Future.failed(ExistingUrlKeyException(key))
      }

  def fetchAll(page: Int, pageSize: Int)(implicit executionContext: ExecutionContext): Future[List[ReadOnlyUrl]] =
    urlDao.fetchAll(page, pageSize).map(_.map(ReadOnlyUrl.fromUrl))

  def delete(key: String, deleteSecret: UUID)(implicit executionContext: ExecutionContext): Future[ReadOnlyUrl] =
    urlDao
      .fetch(key)
      .flatMap { url =>
        if (url.deleteSecret == deleteSecret)
          urlDao.delete(key)
        else
          FutureOpt[Url, Future, Option](Future.failed(IncorrectDeleteSecretException))
      }
      .value
      .flatMap(mapper(key))

  def initialize()(implicit executionContext: ExecutionContext): FutureOpt[InitializationResult] =
    urlDao.initialize()

  private def insert(longUrl: String, suffix: String, keySize: Int, remaining: Int)(
    implicit executionContext: ExecutionContext
  ): Future[Either[ReadOnlyUrl, Url]] =
    for {
      key <- hashingService.hashWithFixedLength(longUrl + suffix, keySize)
      existingUrl <- urlDao.fetch(key).value

      result <- existingUrl
        .fold[Future[Either[ReadOnlyUrl, Url]]](
          urlDao.insert(Url(key, providers.dateTime(), longUrl, 0, providers.uuid())).map(Right.apply)
        ) { url =>
          (url.longUrl, remaining) match {
            case (`longUrl`, _) => Future.successful(Left(ReadOnlyUrl.fromUrl(url)))
            case (_, 0) => insert(longUrl, suffix + key, keySize + 1, serviceConfiguration.fixedKeyLengthRetries)
            case _ => insert(longUrl, suffix + key, keySize, remaining - 1)
          }
        }
    } yield result
}

object UrlShorteningService {
  def mapper(key: String): PartialFunction[Option[Url], Future[ReadOnlyUrl]] = {
    case Some(url) => Future.successful(ReadOnlyUrl.fromUrl(url))
    case _ => Future.failed(MissingUrlKeyException(key))
  }
}
