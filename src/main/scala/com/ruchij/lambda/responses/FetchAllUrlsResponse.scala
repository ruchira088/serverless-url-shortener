package com.ruchij.lambda.responses
import com.ruchij.services.url.models.ReadOnlyUrl
import play.api.libs.json.{Json, OFormat}

case class FetchAllUrlsResponse(page: Int, pageSize: Int, urls: List[ReadOnlyUrl])

object FetchAllUrlsResponse {
  implicit val fetchAllUrlsResponseFormat: OFormat[FetchAllUrlsResponse] = Json.format[FetchAllUrlsResponse]
}
