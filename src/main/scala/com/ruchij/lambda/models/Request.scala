package com.ruchij.lambda.models

import com.ruchij.general.Constants

import scala.collection.JavaConverters._

class Request(var body: Option[String], var headers: Map[String, AnyRef], var path: String) {
  def this() = this(None, Map.empty, Constants.EMPTY_STRING)

  def setBody(requestBody: String): Unit =
    body = Option(requestBody)

  def setHeaders(requestHeaders: java.util.Map[String, AnyRef]): Unit =
    Option(requestHeaders).foreach {
      headerValues =>
        headers = Map(headerValues.asScala.toList: _*)
    }

  def setPath(requestPath: String): Unit =
    path = requestPath
}
