package com.ruchij.services.url.models
import play.api.libs.json.{Json, OFormat}

case class ServiceConfiguration(keyLength: Int, fixedKeyLengthRetries: Int)

object ServiceConfiguration {
  implicit val serviceConfigurationFormat: OFormat[ServiceConfiguration] =
    Json.format[ServiceConfiguration]

  val default = ServiceConfiguration(5, 50)
}
