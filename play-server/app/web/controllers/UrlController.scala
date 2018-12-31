package web.controllers

import com.ruchij.lambda.handlers.UrlInsertionHandler
import com.ruchij.services.url.UrlShorteningService
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import web.requests.RequestUtils.requestMapper
import web.responses.ResponseUtils

import scala.concurrent.ExecutionContext

@Singleton
class UrlController @Inject()(
  urlShortenerHandler: UrlInsertionHandler,
  urlShorteningService: UrlShorteningService,
  controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext)
    extends AbstractController(controllerComponents) {

  def insert(): Action[JsValue] =
    Action.async(parse.json) { request =>
      urlShortenerHandler.insert(request, urlShorteningService)
        .map(ResponseUtils.responseMapper)
    }
}