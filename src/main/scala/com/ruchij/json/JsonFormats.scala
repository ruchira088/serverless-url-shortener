package com.ruchij.json

import com.ruchij.exceptions.ValidationException
import org.joda.time.DateTime
import play.api.libs.json._

import scala.util.Try

object JsonFormats {

  implicit val dateTimeFormat: Format[DateTime] = new Format[DateTime] {

    override def writes(dateTime: DateTime): JsValue = JsString(dateTime.toString)

    override def reads(json: JsValue): JsResult[DateTime] =
      json match {
        case JsString(jsonString) =>
          Try(DateTime.parse(jsonString))
            .fold(throwable => JsError(throwable.getMessage), dateTime => JsSuccess(dateTime))

        case _ => JsError(ValidationException("Must be JSON string").description)
      }
  }
}