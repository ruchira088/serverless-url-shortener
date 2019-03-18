package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.{HTTP_ACCEPTED, HTTP_CREATED}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.UrlInsertionHandler.insert
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.requests.InsertUrlRequest
import com.ruchij.lambda.requests.RequestUtils.parseAndValidate
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UrlInsertionHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result(
      insert(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService, ServiceConfiguration.default)
      ),
      Duration.Inf
    )
}

object UrlInsertionHandler {
  def insert(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    handleExceptions {
      for {
        insertUrlRequest <- Future.fromTry(parseAndValidate[InsertUrlRequest](request))
        url <- insertUrlRequest.key.fold(urlShorteningService.insert(insertUrlRequest.url)) {
          key => urlShorteningService.insert(key, insertUrlRequest.url)
        }
      } yield
        Response(
          if (url.isRight) HTTP_CREATED else HTTP_ACCEPTED,
          Json.toJsObject(url.fold(identity, identity)),
          Map.empty[String, AnyRef]
        )
    }

}
