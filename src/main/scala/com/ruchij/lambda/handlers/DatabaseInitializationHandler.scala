package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.{HTTP_CREATED, HTTP_OK}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.responses.ResponseHandler.handleExceptions
import com.ruchij.lambda.responses.Table
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json
import slick.jdbc.meta.MTable

import scala.concurrent.{ExecutionContext, Future}

class DatabaseInitializationHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response = ???
}

object DatabaseInitializationHandler {
  def initialize(request: Request, urlShorteningService: UrlShorteningService)(implicit executionContext: ExecutionContext): Future[Response] =
    handleExceptions {
      urlShorteningService.initialize().map[Response] {
        table =>
          Response(
            table.fold(_ => HTTP_OK, _ => HTTP_CREATED),
            Json.toJsObject(Table.fromMTable(table.fold[MTable](identity, identity))),
            Map.empty[String, AnyRef]
          )
      }
    }
}
