package com.ruchij.lambda.models

import play.api.libs.json.{JsObject, Json}

import scala.collection.JavaConverters._

case class Response(statusCode: Int, body: JsObject, headers: Map[String, AnyRef]) {
  def getStatusCode: Integer = statusCode

  def getBody: String = Json.stringify(body)

  def getHeaders: java.util.Map[String, AnyRef] = headers.asJava
}
