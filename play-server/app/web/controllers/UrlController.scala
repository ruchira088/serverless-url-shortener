package web.controllers

import com.ruchij.lambda.handlers.{UrlInfoHandler, UrlInsertionHandler, UrlRedirectHandler}
import com.ruchij.services.url.UrlShorteningService
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import web.requests.RequestUtils.requestMapper
import web.responses.ResponseUtils.toResultFuture

import scala.concurrent.ExecutionContext

@Singleton
class UrlController @Inject()(
  urlShortenerHandler: UrlInsertionHandler,
  urlInfoHandler: UrlInfoHandler,
  urlRedirectHandler: UrlRedirectHandler,
  urlShorteningService: UrlShorteningService,
  controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext)
    extends AbstractController(controllerComponents) {

  def insert(): Action[JsValue] =
    Action.async(parse.json) { request =>
      urlShortenerHandler.insert(request, urlShorteningService)
    }

  def info(key: String): Action[AnyContent] =
    Action.async {
      request => urlInfoHandler.info(request, urlShorteningService)
    }

  def redirect(key: String): Action[AnyContent] =
    Action.async {
      request => urlRedirectHandler.redirect(request, urlShorteningService)
    }
}