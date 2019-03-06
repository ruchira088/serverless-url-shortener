package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.HTTP_OK

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.dao.{InMemoryUrlDao, SlickUrlDao}
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.HandlerUtils._
import com.ruchij.lambda.handlers.UrlInfoHandler.info
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import com.ruchij.services.url.models.ServiceConfiguration
import play.api.libs.json.Json

import scala.concurrent.Future.fromTry
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UrlInfoHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result(
      info(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService(), ServiceConfiguration.default)
      ),
      Duration.Inf
    )
}

object UrlInfoHandler {
  def info(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    for {
      key <- fromTry { extractKey(keyRegex(InfoPrefix))(request.path) }
      url <- urlShorteningService.info(key)
    } yield Response(HTTP_OK, Json.toJsObject(url), Map.empty)
}
