package web.responses
import com.ruchij.eed3si9n.play.BuildInfo
import play.api.libs.json.{Json, OFormat}

import scala.util.Properties

case class ServiceInformation(
  name: String,
  organization: String,
  version: String,
  javaVersion: String,
  sbtVersion: String,
  scalaVersion: String
)

object ServiceInformation {
  implicit val serviceInformationFormat: OFormat[ServiceInformation] = Json.format[ServiceInformation]

  def apply(): ServiceInformation =
    ServiceInformation(
      BuildInfo.name,
      BuildInfo.organization,
      BuildInfo.version,
      Properties.javaVersion,
      BuildInfo.sbtVersion,
      BuildInfo.scalaVersion
    )

}
