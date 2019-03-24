package com.ruchij.dao
import java.util.concurrent.ConcurrentHashMap

import com.ruchij.FutureOpt
import com.ruchij.exceptions.InMemoryDatabasePersistenceException
import com.ruchij.general.Orderings.DateTimeOrdering
import com.ruchij.monad.FoldableMonadInMonad
import com.ruchij.monad.Monad.futureMonad
import com.ruchij.monad.FoldableMonad.OptionMonad
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
      .flatMapM { url =>
        insert(url.copy(hits = url.hits + 1))
          .map { newUrl =>
            concurrentHashMap.remove(url)
            newUrl
          }
      }

  override def fetchAll(page: Int, pageSize: Int)(implicit executionContext: ExecutionContext): Future[List[Url]] =
    Future.successful {
      concurrentHashMap
        .keys()
        .asScala
        .toList
        .sortBy(_.createdAt)
        .slice(page * pageSize, pageSize * (page + 1))
    }

  override def delete(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key).map {
      url =>
        concurrentHashMap.remove(url.key)
        url
    }
}

object InMemoryUrlDao {
  def apply(): InMemoryUrlDao = new InMemoryUrlDao(new ConcurrentHashMap[Url, Unit]())
}
