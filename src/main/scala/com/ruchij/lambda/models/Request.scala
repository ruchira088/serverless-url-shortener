package com.ruchij.lambda.models

import com.ruchij.general.Messages

import scala.collection.JavaConverters._
import scala.language.existentials

class Request(
  var body: Option[String],
  var headers: Map[String, Seq[String]],
  var path: String,
  var pathParameters: Map[String, String],
  var queryParameters: Map[String, Seq[String]]
) {

  def this() = this(None, Map.empty, Messages.EMPTY_STRING, Map.empty[String, String], Map.empty[String, List[String]])

  def setBody(requestBody: String): Unit =
    body = Option(requestBody)

  def setHeaders(requestHeaders: java.util.Map[String, String]): Unit =
    Option(requestHeaders)
      .foreach { headerValues =>
        headers =
          headerValues.asScala
            .map { case (key, value) => key -> List(value) }
            .toMap
      }

  def setPathParameters(parameters: java.util.Map[String, String]): Unit =
    pathParameters = parameters.asScala.toMap

  def withPathParameter(parameter: (PathParameter[_], String) ): Request = {
    val (pathParameter, value) = parameter
    new Request(body, headers, path, pathParameters ++ Map(pathParameter.key -> value), queryParameters)
  }

  def setPath(requestPath: String): Unit =
    path = requestPath

  def setQueryStringParameters(parameters: java.util.Map[String, String]): Unit =
    Option(parameters)
      .foreach { queryStringParameters =>
        queryParameters =
          queryStringParameters.asScala
            .map { case (key, value) => key -> List(value) }
            .toMap
      }
}
