package com.ruchij.services.url.models

import com.ruchij.json.JsonFormats.dateTimeFormat
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

case class Url(key: String, createdAt: DateTime, longUrl: String, hits: Double)

object Url {
  implicit val urlFormat: OFormat[Url] = Json.format[Url]
}
