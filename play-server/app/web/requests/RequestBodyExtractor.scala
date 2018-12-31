package web.requests
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContent, Request}

trait RequestBodyExtractor[-A] {
  def extract[B <: A](request: Request[B]): Option[String]
}

object RequestBodyExtractor {
  implicit object JsonRequestBodyExtractor extends RequestBodyExtractor[JsValue] {
    override def extract[B <: JsValue](request: Request[B]): Option[String] =
      Some(Json.stringify(request.body))
  }

  implicit object AnyContentRequestBodyExtractor extends RequestBodyExtractor[AnyContent] {
    override def extract[B <: AnyContent](request: Request[B]): Option[String] =
      request.body.asText
  }
}
