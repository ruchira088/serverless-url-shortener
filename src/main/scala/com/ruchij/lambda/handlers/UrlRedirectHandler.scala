package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.HTTP_MOVED_TEMP

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.google.common.net.HttpHeaders
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.HandlerUtils._
import com.ruchij.lambda.handlers.UrlRedirectHandler.redirect
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.providers.Providers
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.Future.fromTry
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UrlRedirectHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result(
      redirect(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService)(ServiceConfiguration.default, Providers)
      ),
      Duration.Inf
    )
}

object UrlRedirectHandler {
  def redirect(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    handleExceptions {
      for {
        key <- fromTry { extractKey(RedirectPrefix)(request.path) }
        url <- urlShorteningService.fetch(key)
      } yield Response(HTTP_MOVED_TEMP, Json.obj(), Map(HttpHeaders.LOCATION -> url.longUrl))
    }
}
