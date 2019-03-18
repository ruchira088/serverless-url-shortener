package com.ruchij.config.elasticsearch

import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.http.ElasticDsl._

case class ElasticSearchConfiguration(sslEnabled: Boolean, host: String, port: Int, index: String, `type`: String)

object ElasticSearchConfiguration {
  def url(elasticSearchConfiguration: ElasticSearchConfiguration): String =
    s"${if (elasticSearchConfiguration.sslEnabled) "https" else "http"}://${elasticSearchConfiguration.host}:${elasticSearchConfiguration.port}"

  def indexAndType(elasticSearchConfiguration: ElasticSearchConfiguration): IndexAndType =
    elasticSearchConfiguration.index / elasticSearchConfiguration.`type`
}
