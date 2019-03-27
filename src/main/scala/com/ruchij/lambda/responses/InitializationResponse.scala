package com.ruchij.lambda.responses
import com.ruchij.dao.models.InitializationResult
import play.api.libs.json.{Json, OWrites}

case class InitializationResponse(result: String, message: Option[InitializationResult])

object InitializationResponse {
  implicit val initializationResponseWrites: OWrites[InitializationResponse] = Json.writes[InitializationResponse]
}
