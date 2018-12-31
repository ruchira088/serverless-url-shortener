package com.ruchij.lambda.handlers
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.services.url.UrlShorteningService

import scala.concurrent.{ExecutionContext, Future}

class UrlFetchHandler extends RequestHandler[Request, Response] {
  override def handleRequest(input: Request, context: Context): Response = ???

  def fetch(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] = ???
}

object UrlFetchHandler {
  def extractKey(request: Request): Option[String] =
    request.path match {

    }
}
