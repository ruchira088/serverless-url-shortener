package web.responses
import com.ruchij.eed3si9n.play.BuildInfo
import play.api.libs.json.{Json, OFormat}

import scala.util.Properties

case class PlayServerInformation(
  name: String,
  organization: String,
  version: String,
  javaVersion: String,
  sbtVersion: String,
  scalaVersion: String
)

object PlayServerInformation {
  implicit val playServerInformationFormat: OFormat[PlayServerInformation] = Json.format[PlayServerInformation]

  def apply(): PlayServerInformation =
    PlayServerInformation(
      BuildInfo.name,
      BuildInfo.organization,
      BuildInfo.version,
      Properties.javaVersion,
      BuildInfo.sbtVersion,
      BuildInfo.scalaVersion
    )
}
