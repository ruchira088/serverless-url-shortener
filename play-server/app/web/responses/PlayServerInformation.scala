package web.responses

import com.ruchij.eed3si9n.play.BuildInfo
import com.ruchij.json.JsonFormats.dateTimeFormat
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

import scala.util.Properties

case class PlayServerInformation(
  name: String,
  organization: String,
  version: String,
  javaVersion: String,
  sbtVersion: String,
  scalaVersion: String,
  timestamp: DateTime
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
      BuildInfo.scalaVersion,
      DateTime.now()
    )
}
