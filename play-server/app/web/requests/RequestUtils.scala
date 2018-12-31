package web.requests

import com.ruchij.lambda.models.Request
import play.api.libs.json.{JsValue, Json}
import play.api.mvc

import scala.language.implicitConversions

object RequestUtils {

  implicit def requestMapper(request: mvc.Request[JsValue]): Request =
    new Request(Some(Json.stringify(request.body)), request.headers.toMap, request.path)
}