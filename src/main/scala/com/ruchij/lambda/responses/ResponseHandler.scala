package com.ruchij.lambda.responses
import java.net.HttpURLConnection.{HTTP_CONFLICT, HTTP_INTERNAL_ERROR, HTTP_NOT_FOUND}

import com.ruchij.exceptions.{ExistingUrlKeyException, MissingUrlKeyException}
import com.ruchij.lambda.models.Response

import scala.concurrent.{ExecutionContext, Future}

object ResponseHandler {
  val emptyHeaders: Map[String, AnyRef] = Map.empty

  def handleExceptions(result: Future[Response])(implicit executionContext: ExecutionContext): Future[Response] =
    result.recover {
      case existingUrlKeyException: ExistingUrlKeyException =>
        Response(HTTP_CONFLICT, existingUrlKeyException, emptyHeaders)

      case missingUrlKeyException: MissingUrlKeyException =>
        Response(HTTP_NOT_FOUND, missingUrlKeyException, emptyHeaders)

      case uncaughtException =>
        Response(HTTP_INTERNAL_ERROR, uncaughtException, emptyHeaders)
    }
}
