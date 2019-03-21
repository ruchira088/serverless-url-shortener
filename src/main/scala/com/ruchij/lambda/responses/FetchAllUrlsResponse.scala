package com.ruchij.lambda.responses
import com.ruchij.services.url.models.Url
import play.api.libs.json.{Json, OFormat}

case class FetchAllUrlsResponse(page: Int, pageSize: Int, urls: List[Url])

object FetchAllUrlsResponse {
  implicit val fetchAllUrlsResponseFormat: OFormat[FetchAllUrlsResponse] = Json.format[FetchAllUrlsResponse]
}
