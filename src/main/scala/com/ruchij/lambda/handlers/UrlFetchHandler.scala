package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.HTTP_OK

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.dao.InMemoryUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.exceptions.ValidationException
import com.ruchij.lambda.handlers.UrlFetchHandler.extractKey
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.Future.fromTry
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

class UrlFetchHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result(fetch(request, new UrlShorteningService(InMemoryUrlDao(), new MurmurHashingService())), Duration.Inf)

  def fetch(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    for {
      key <- fromTry(extractKey(request.path))
      url <- urlShorteningService.fetch(key)
    }
    yield Response(HTTP_OK, Json.toJsObject(url), Map.empty)
}

object UrlFetchHandler {
  private val urlKeyRegex: Regex = """\/url\/([A-Za-z0-9_\-\.\+]+)""".r

  def extractKey: PartialFunction[String, Try[String]] = {
      case urlKeyRegex(key) => Success(key)
      case path => Failure(ValidationException(s"Unable to extract key from $path"))
    }
}
