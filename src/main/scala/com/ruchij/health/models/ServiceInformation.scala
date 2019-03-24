package com.ruchij.health.models

import com.ruchij.eed3si9n.BuildInfo
import com.ruchij.json.JsonFormats.dateTimeFormat
import com.ruchij.providers.TimestampProvider
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

import scala.util.Properties

case class ServiceInformation(
  name: String,
  organization: String,
  version: String,
  javaVersion: String,
  scalaVersion: String,
  sbtVersion: String,
  timestamp: DateTime
)

object ServiceInformation {
  implicit val serviceInformationFormat: OFormat[ServiceInformation] = Json.format[ServiceInformation]

  def apply(implicit timestampProvider: TimestampProvider): ServiceInformation =
    ServiceInformation(
      BuildInfo.name,
      BuildInfo.organization,
      BuildInfo.version,
      Properties.javaVersion,
      BuildInfo.scalaVersion,
      BuildInfo.sbtVersion,
      timestampProvider.dateTime()
    )
}
