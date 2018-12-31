package web.responses

import com.ruchij.health
import play.api.libs.json.{Json, OFormat}

case class ServiceInformation(
  playServerInformation: PlayServerInformation,
  serverlessServiceInformation: health.models.ServiceInformation
)

object ServiceInformation {
  implicit val serviceInformationFormat: OFormat[ServiceInformation] = Json.format[ServiceInformation]

  def apply(): ServiceInformation =
    ServiceInformation(PlayServerInformation(), health.models.ServiceInformation())
}
