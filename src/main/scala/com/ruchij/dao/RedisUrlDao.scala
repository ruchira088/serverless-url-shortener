package com.ruchij.dao
import com.ruchij.FutureOpt
import com.ruchij.services.url.models.Url
import javax.inject.{Inject, Singleton}
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedisUrlDao @Inject()(redisClient: RedisClient) extends UrlDao {
  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] =
    redisClient.set(url.key, url)
      .flatMap {
        _ => fetch(url.key).flatten
      }

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FutureOpt {
      redisClient.get[Url](key)
    }

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key)
      .flatMapM {
        url => insert(url.copy(hits = url.hits + 1))
      }
}
