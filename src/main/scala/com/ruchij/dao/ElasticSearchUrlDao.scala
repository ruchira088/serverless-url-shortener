package com.ruchij.dao

import com.ruchij.FutureOpt
import com.ruchij.config.elasticsearch.ElasticSearchConfiguration
import com.ruchij.config.elasticsearch.ElasticSearchConfiguration.indexAndType
import com.ruchij.exceptions.ElasticException
import com.ruchij.services.url.models.Url
import com.sksamuel.elastic4s.http.ElasticClient
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.playjson._
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ElasticSearchUrlDao @Inject()(elasticClient: ElasticClient, elasticSearchConfiguration: ElasticSearchConfiguration) extends UrlDao {
  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] =
    elasticClient
      .execute { indexInto(indexAndType(elasticSearchConfiguration)).doc(url) id url.key }
      .flatMap { response =>
        response.fold[Future[Url]](Future.failed(ElasticException(response.error))) { _ =>
          Future.successful(url)
        }
      }

//  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
//    FutureOpt {
//      elasticClient
//        .execute { searchWithType(indexAndType(elasticSearchConfiguration)).matchQuery("key", key) }
//        .map {
//          searchResponse => searchResponse.result.to[Url].headOption
//        }
//    }

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    FutureOpt {
      elasticClient
        .execute { get(key).from(indexAndType(elasticSearchConfiguration)) }
        .flatMap {
          _.result.safeToOpt[Url].fold[Future[Option[Url]]](Future.successful(None)) { result =>
            Future.fromTry(result).map(Some.apply)
          }
        }
    }

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    fetch(key)
      .flatMapM { url =>
        val updatedUrl = url.copy(hits = url.hits + 1)
        elasticClient.execute { update(key).in(indexAndType(elasticSearchConfiguration)).doc(updatedUrl) }
          .flatMap {
            response =>
              response.fold[Future[Url]](Future.failed(ElasticException(response.error))) {
              _ => Future.successful(updatedUrl)
            }
          }
      }
}
