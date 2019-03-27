package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.{HTTP_CREATED, HTTP_OK}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.HandlerUtils.await
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.requests.InsertUrlRequest
import com.ruchij.lambda.requests.RequestUtils.parseAndValidate
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.providers.Providers
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.{ExecutionContext, Future}

class UrlInsertionHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    await {
      UrlInsertionHandler.insert(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService)(ServiceConfiguration.default, Providers)
      )
    }
}

object UrlInsertionHandler {
  def insert(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    handleExceptions {
      for {
        insertUrlRequest <- Future.fromTry(parseAndValidate[InsertUrlRequest](request))
        url <- insertUrlRequest.key.fold(urlShorteningService.insert(insertUrlRequest.url)) { key =>
          urlShorteningService.insert(key, insertUrlRequest.url)
        }
      } yield
        Response(
          if (url.isRight) HTTP_CREATED else HTTP_OK,
          url.fold[JsObject](Json.toJsObject, Json.toJsObject),
          Map.empty[String, AnyRef]
        )
    }

}
