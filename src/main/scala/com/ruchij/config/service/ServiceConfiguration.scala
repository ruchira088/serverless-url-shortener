package com.ruchij.config.service

import play.api.libs.json.{Json, OFormat}

case class ServiceConfiguration(keyLength: Int, fixedKeyLengthRetries: Int, pageSize: Int)

object ServiceConfiguration {
  implicit val serviceConfigurationFormat: OFormat[ServiceConfiguration] =
    Json.format[ServiceConfiguration]

  val default = ServiceConfiguration(5, 50, 10)
}
