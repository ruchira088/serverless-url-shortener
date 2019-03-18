package com.ruchij.lambda.responses
import java.net.HttpURLConnection.{HTTP_CONFLICT, HTTP_NOT_FOUND}

import com.ruchij.exceptions.{ExistingUrlKeyException, MissingUrlKeyException}
import com.ruchij.lambda.models.Response

import scala.concurrent.{ExecutionContext, Future}

object ResponseHandler {
  def handleExceptions(result: Future[Response])(implicit executionContext: ExecutionContext): Future[Response] =
    result.recover {
      case existingUrlKeyException: ExistingUrlKeyException =>
        Response(HTTP_CONFLICT, existingUrlKeyException, Map.empty[String, AnyRef])

      case missingUrlKeyException: MissingUrlKeyException =>
        Response(HTTP_NOT_FOUND, missingUrlKeyException, Map.empty[String, AnyRef])
    }
}
