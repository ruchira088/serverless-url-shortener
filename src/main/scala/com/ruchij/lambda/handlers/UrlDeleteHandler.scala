package com.ruchij.lambda.handlers
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.services.url.UrlShorteningService

import scala.concurrent.{ExecutionContext, Future}

class UrlDeleteHandler extends RequestHandler[Request, Response] {
  override def handleRequest(input: Request, context: Context): Response = ???
}

object UrlDeleteHandler {
//  def delete(request: Request, urlShorteningService: UrlShorteningService)(
//    implicit executionContext: ExecutionContext
//  ): Future[Response] =
//    handleExceptions {
//      for {
//
//      }
//      yield ???
//    }
}
