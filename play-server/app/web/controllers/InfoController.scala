package web.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import web.responses.ServiceInformation

@Singleton
class InfoController @Inject()(controllerComponents: ControllerComponents)
    extends AbstractController(controllerComponents) {

  def serviceInformation(): Action[AnyContent] =
    Action {
      Ok {
        Json.toJsObject(ServiceInformation())
      }
    }
}