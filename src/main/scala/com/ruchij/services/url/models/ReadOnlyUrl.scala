package com.ruchij.services.url.models

import com.ruchij.json.JsonFormats.dateTimeFormat
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

case class ReadOnlyUrl(key: String, createdAt: DateTime, longUrl: String, hits: Double)

object ReadOnlyUrl {
  implicit val readOnlyUrlFormat: OFormat[ReadOnlyUrl] = Json.format[ReadOnlyUrl]

  def fromUrl(url: Url): ReadOnlyUrl =
    ReadOnlyUrl(url.key, url.createdAt, url.longUrl, url.hits)
}
