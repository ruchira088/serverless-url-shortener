package web.responses

import com.ruchij.lambda.models.Response
import play.api.mvc.Result
import play.api.mvc.Results.Status

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

object ResponseUtils {

  def responseMapper(response: Response): Result =
    new Status(response.statusCode)(response.body)
      .withHeaders(response.headers.collect {
        case (key, value: String) => key -> value
      }.toSeq: _*)

  implicit def toResultFuture(
    responseFuture: Future[Response]
  )(implicit executionContext: ExecutionContext): Future[Result] =
    responseFuture.map(responseMapper)
}
