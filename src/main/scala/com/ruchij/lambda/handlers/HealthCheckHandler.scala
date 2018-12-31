package com.ruchij.lambda.handlers

import java.net.HttpURLConnection

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.ruchij.lambda.models.{Request, Response}
import com.ruchij.health.models.ServiceInformation
import play.api.libs.json.Json

class HealthCheckHandler extends RequestHandler[Request, Response] {
  override def handleRequest(request: Request, context: Context): Response =
    Response(
      HttpURLConnection.HTTP_OK,
      Json.toJsObject(ServiceInformation()),
      Map.empty
    )
}