package com.ruchij.lambda.handlers

import java.net.HttpURLConnection.HTTP_OK

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.dao.SlickUrlDao
import com.ruchij.ec.ServerlessBlockExecutionContext.blockingExecutionContext
import com.ruchij.lambda.handlers.UrlFetchAllHandler.fetchAll
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.lambda.requests.{QueryParameter, QueryParameterParser}
import com.ruchij.lambda.responses.FetchAllUrlsResponse
import com.ruchij.services.hashing.MurmurHashingService
import com.ruchij.services.url.UrlShorteningService
import play.api.libs.json.Json

import scala.concurrent.Future.fromTry
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UrlFetchAllHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Await.result(
      fetchAll(
        request,
        new UrlShorteningService(SlickUrlDao(), new MurmurHashingService(), ServiceConfiguration.default)
      ),
      Duration.Inf
    )
}

object UrlFetchAllHandler {
  def fetchAll(request: Request, urlShorteningService: UrlShorteningService)(
    implicit executionContext: ExecutionContext
  ): Future[Response] =
    for {
      pageSize <- fromTry {
        QueryParameter.PageSize.parse(request, urlShorteningService.serviceConfiguration.pageSize)
      }
      page <- fromTry { QueryParameter.Page.parse(request, 0) }
      urls <- urlShorteningService.fetchAll(page, pageSize)
    } yield Response(HTTP_OK, Json.toJsObject(FetchAllUrlsResponse(page, pageSize, urls)), Map.empty[String, AnyRef])
}
