package com.ruchij.services.url.models

import java.util.UUID

import akka.util.ByteString
import com.ruchij.json.JsonFormats.dateTimeFormat
import org.joda.time.DateTime
import play.api.libs.json.{JsResultException, Json, OFormat}
import redis.ByteStringFormatter

case class Url(key: String, createdAt: DateTime, longUrl: String, hits: Double, deleteSecret: UUID)

object Url {
  implicit val urlFormat: OFormat[Url] = Json.format[Url]

  implicit val urlByteStringFormatter: ByteStringFormatter[Url] =
    new ByteStringFormatter[Url] {
      override def deserialize(byteString: ByteString): Url =
        Json.fromJson[Url](Json.parse(byteString.utf8String))
          .fold[Url](errors => throw JsResultException(errors), identity)

      override def serialize(url: Url): ByteString = ByteString { Json.stringify(Json.toJson(url)) }
    }
}
