package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.{HTTP_ACCEPTED, HTTP_CREATED}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.dao.InMemoryUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.requests.InsertUrlRequest
import com.ruchij.lambda.requests.RequestUtils.parseAndValidate
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UrlShortenerHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result({
      implicit val blockingExecutionContext: ServerlessBlockExecutionContext =
        new ServerlessBlockExecutionContext()(ExecutionContext.Implicits.global)

      handle(request, new UrlShorteningService(InMemoryUrlDao(), new MurmurHashingService))
    }, Duration.Inf)

  def handle(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    for {
      insertUrlRequest <- Future.fromTry(parseAndValidate[InsertUrlRequest](request))
      url <- urlShorteningService.insert(insertUrlRequest.url)
    } yield
      Response(
        if (url.isRight) HTTP_CREATED else HTTP_ACCEPTED,
        Json.toJsObject(url.fold(identity, identity)),
        Map.empty
      )
}
