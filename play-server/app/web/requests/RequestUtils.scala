package web.requests

import com.ruchij.lambda.models.Request
import play.api.mvc

import scala.language.implicitConversions

object RequestUtils {

  implicit def requestMapper[A](
    request: mvc.Request[A]
  )(implicit requestBodyExtractor: RequestBodyExtractor[A]): Request =
    new Request(requestBodyExtractor.extract(request), request.headers.toMap, request.path, request.queryString)
}
