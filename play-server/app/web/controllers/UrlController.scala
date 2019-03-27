package web.controllers

import com.ruchij.lambda.handlers._
import com.ruchij.lambda.models.PathParameter.UrlKey
import com.ruchij.services.url.UrlShorteningService
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import web.requests.RequestUtils.requestMapper
import web.responses.ResponseUtils.toResultFuture

import scala.concurrent.ExecutionContext

@Singleton
class UrlController @Inject()(urlShorteningService: UrlShorteningService, controllerComponents: ControllerComponents)(
  implicit executionContext: ExecutionContext
) extends AbstractController(controllerComponents) {

  def initialize(): Action[AnyContent] =
    Action.async { request =>
      DatabaseInitializationHandler.initialize(request, urlShorteningService)
    }

  def insert(): Action[JsValue] =
    Action.async(parse.json) { request =>
      UrlInsertionHandler.insert(request, urlShorteningService)
    }

  def info(key: String): Action[AnyContent] =
    Action.async { request =>
      UrlInfoHandler.info(request.withPathParameter(UrlKey -> key), urlShorteningService)
    }

  def redirect(key: String): Action[AnyContent] =
    Action.async { request =>
      UrlRedirectHandler.redirect(request.withPathParameter(UrlKey -> key), urlShorteningService)
    }

  def fetchAll(): Action[AnyContent] =
    Action.async { request =>
      UrlFetchAllHandler.fetchAll(request, urlShorteningService)
    }
}
