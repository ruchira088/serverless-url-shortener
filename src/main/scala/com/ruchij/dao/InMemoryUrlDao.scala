package com.ruchij.dao
import java.util.concurrent.ConcurrentHashMap

import com.ruchij.FutureOpt
import com.ruchij.exceptions.InMemoryDatabasePersistenceException
import com.ruchij.monad.FoldableMonadInMonad
import com.ruchij.services.url.models.Url
import javax.inject.{Inject, Singleton}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InMemoryUrlDao @Inject()(@volatile var concurrentHashMap: ConcurrentHashMap[Url, Unit]) extends UrlDao {
  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] = {
    concurrentHashMap.put(url, (): Unit)
    Option(concurrentHashMap.get(url))
      .fold[Future[Url]](Future.failed(InMemoryDatabasePersistenceException(url)))(_ => Future.successful(url))
  }

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FoldableMonadInMonad {
      Future.successful {
        concurrentHashMap.keys().asScala.find(_.key == key)
      }
    }

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key)
      .flatMapM {
        url =>
          insert(url.copy(hits = url.hits + 1))
            .map {
              newUrl =>
                concurrentHashMap.remove(url)
                newUrl
            }
      }
}

object InMemoryUrlDao {
  def apply(): InMemoryUrlDao = new InMemoryUrlDao(new ConcurrentHashMap[Url, Unit]())
}
