package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.config.elasticsearch.ElasticSearchConfiguration
import com.ruchij.configuration.ConfigLoader
import com.ruchij.dao.{ElasticSearchUrlDao, UrlDao}
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.index.CreateIndexResponse
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties, Response}
import javax.inject.Singleton
import play.api.Configuration

import scala.concurrent.Future

class ElasticSearchModule extends AbstractModule {

  @Singleton
  @Provides
  def elasticSearchConfiguration(implicit configuration: Configuration): ElasticSearchConfiguration =
    ConfigLoader.parse[ElasticSearchConfiguration](configuration.underlying).get

  @Singleton
  @Provides
  def elasticClient(elasticSearchConfiguration: ElasticSearchConfiguration): ElasticClient =
    ElasticClient(ElasticProperties(ElasticSearchConfiguration.url(elasticSearchConfiguration)))

  @Singleton
  @Provides
  def elasticSearchUrlDao(elasticClient: ElasticClient, elasticSearchConfiguration: ElasticSearchConfiguration): UrlDao = {
    createElasticSearchIndex(elasticClient, elasticSearchConfiguration).await
    new ElasticSearchUrlDao(elasticClient, elasticSearchConfiguration)
  }

  def createElasticSearchIndex(elasticClient: ElasticClient, elasticSearchConfiguration: ElasticSearchConfiguration): Future[Response[CreateIndexResponse]] =
    elasticClient.execute {
      createIndex(elasticSearchConfiguration.index).mappings {
        mapping(elasticSearchConfiguration.`type`)
      } shards 3
    }
}
