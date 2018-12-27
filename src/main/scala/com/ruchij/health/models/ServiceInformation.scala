package com.ruchij.health.models

import com.ruchij.eed3si9n.BuildInfo
import play.api.libs.json.{Json, OFormat}

import scala.util.Properties

case class ServiceInformation(
  name: String,
  organization: String,
  version: String,
  javaVersion: String,
  scalaVersion: String,
  sbtVersion: String
)

object ServiceInformation {
  implicit val serviceInformationFormat: OFormat[ServiceInformation] = Json.format[ServiceInformation]

  def apply(): ServiceInformation =
    ServiceInformation(
      BuildInfo.name,
      BuildInfo.organization,
      BuildInfo.version,
      Properties.javaVersion,
      BuildInfo.scalaVersion,
      BuildInfo.sbtVersion
    )
}
