package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.HTTP_OK

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.HandlerUtils._
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.Future.fromTry
import scala.concurrent.{ExecutionContext, Future}

class UrlInfoHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    await {
      UrlInfoHandler.info(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService(), ServiceConfiguration.default)
      )
    }
}

object UrlInfoHandler {
  def info(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    handleExceptions {
      for {
        key <- fromTry { extractKey(InfoPrefix)(request.path) }
        url <- urlShorteningService.info(key)
      } yield Response(HTTP_OK, Json.toJsObject(url), Map.empty[String, AnyRef])
    }
}
