package com.ruchij.lambda.requests

import com.ruchij.exceptions.{AggregatedValidationException, NonExistentRequestBodyException, ValidationException}
import com.ruchij.lambda.models.Request
import play.api.libs.json.{JsValue, Json, Reads}

import scala.util.{Failure, Success, Try}

object RequestUtils {

  // I just wanna role my sleeves up, and start again
  def parseAndValidate[A](request: Request)(implicit reads: Reads[A], validator: Validator[A]): Try[A] =
    request.body.fold[Try[A]](Failure(NonExistentRequestBodyException)) { jsonString =>
      Try(Json.parse(jsonString))
        .flatMap {
          _.validate[A].asEither.left
            .map {
              _.flatMap {
                case (jsPath, validationErrors) =>
                  validationErrors.map { validationError =>
                    s"${validationError.message} at $jsPath"
                  }
              }
            }
            .flatMap(validator.validate)
            .fold[Try[A]](
              validationErrors =>
                Failure(AggregatedValidationException(validationErrors.map(ValidationException.apply))),
              Success.apply
            )
        }
    }
}
