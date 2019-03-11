package com.ruchij.lambda.responses
import java.net.HttpURLConnection.HTTP_CONFLICT

import com.ruchij.exceptions.ExistingUrlKeyException
import com.ruchij.lambda.models.Response

import scala.concurrent.{ExecutionContext, Future}

object ResponseHandler {
  def handleExceptions(result: Future[Response])(implicit executionContext: ExecutionContext): Future[Response] =
    result.recover {
      case existingUrlKeyException: ExistingUrlKeyException =>
        Response(HTTP_CONFLICT, existingUrlKeyException, Map.empty[String, AnyRef])
    }
}
