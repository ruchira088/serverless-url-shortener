package web.responses

import com.ruchij.health
import com.ruchij.providers.TimestampProvider
import play.api.libs.json.{Json, OFormat}

case class ServiceInformation(
  playServerInformation: PlayServerInformation,
  serverlessServiceInformation: health.models.ServiceInformation
)

object ServiceInformation {
  implicit val serviceInformationFormat: OFormat[ServiceInformation] = Json.format[ServiceInformation]

  def apply(implicit timestampProvider: TimestampProvider): ServiceInformation =
    ServiceInformation(PlayServerInformation(timestampProvider), health.models.ServiceInformation(timestampProvider))
}
