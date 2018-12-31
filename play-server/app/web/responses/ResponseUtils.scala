package web.responses

import com.ruchij.lambda.models.Response
import play.api.mvc.Result
import play.api.mvc.Results.Status

import scala.language.implicitConversions

object ResponseUtils {

  implicit def responseMapper(response: Response): Result =
    new Status(response.statusCode)(response.body)
      .withHeaders(response.headers.collect {
        case (key, value: String) => key -> value
      }.toSeq: _*)
}
