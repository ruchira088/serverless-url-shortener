package com.ruchij.lambda.models

import play.api.libs.json.{JsObject, Json, OFormat}

import scala.collection.JavaConverters._

case class Response(statusCode: Int, body: JsObject, headers: Map[String, AnyRef]) {
  def getStatusCode: Integer = statusCode

  def getBody: String = Json.stringify(body)

  def getHeaders: java.util.Map[String, AnyRef] = headers.asJava
}

object Response {
  case class ErrorResponse(errorMessage: String)
  implicit val errorResponseFormat: OFormat[ErrorResponse] = Json.format[ErrorResponse]

  def apply(statusCode: Int, throwable: Throwable, headers: Map[String, AnyRef]): Response =
    Response(statusCode, Json.toJsObject(ErrorResponse(throwable.getMessage)), headers)
}
