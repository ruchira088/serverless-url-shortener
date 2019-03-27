package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.{HTTP_CREATED, HTTP_OK}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.general.Messages
import com.ruchij.lambda.handlers.HandlerUtils.await
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.responses.InitializationResponse
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class DatabaseInitializationHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    await {
      DatabaseInitializationHandler.initialize(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService(), ServiceConfiguration.default)
      )
    }
}

object DatabaseInitializationHandler {
  def initialize(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    handleExceptions {
      urlShorteningService.initialize().value
        .map {
          case None => InitializationResponse(Messages.DATABASE_ALREADY_INITIALIZED, None)
          case initializationResult =>
            InitializationResponse(Messages.DATABASE_INITIALIZATION_SUCCESS, initializationResult)
        }
        .map { initializationResponse =>
          Response(
            initializationResponse.message.fold(HTTP_OK)(_ => HTTP_CREATED),
            Json.toJsObject(initializationResponse),
            Map.empty[String, AnyRef]
          )
        }
    }
}
