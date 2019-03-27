package com.ruchij.lambda.models

import com.ruchij.general.Messages

import scala.collection.JavaConverters._

class Request(
  var body: Option[String],
  var headers: Map[String, AnyRef],
  var path: String,
  var queryParameters: Map[String, Seq[String]]
) {
  def this() = this(None, Map.empty, Messages.EMPTY_STRING, Map.empty[String, Seq[String]])

  def setBody(requestBody: String): Unit =
    body = Option(requestBody)

  def setHeaders(requestHeaders: java.util.Map[String, AnyRef]): Unit =
    Option(requestHeaders)
      .foreach { headerValues =>
        headers = headerValues.asScala.toMap
      }

  def setPath(requestPath: String): Unit =
    path = requestPath

  def setQueryStringParameters(parameters: java.util.Map[String, String]): Unit =
    Option(parameters)
      .foreach { queryStringParameters =>
        queryParameters = queryStringParameters.asScala.map { case (key, value) => key -> List(value) }.toMap
      }
}
