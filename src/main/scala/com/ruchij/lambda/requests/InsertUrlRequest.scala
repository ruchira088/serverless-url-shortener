package com.ruchij.lambda.requests

import com.ruchij.exceptions.ValidationException
import com.ruchij.monad.Monad.monadSequence
import com.ruchij.monad.Monad.TryMonad.predicate
import play.api.libs.json.{Json, OFormat}
case class InsertUrlRequest(url: String)

object InsertUrlRequest  {
  implicit val insertUrlRequestFormat: OFormat[InsertUrlRequest] = Json.format[InsertUrlRequest]

  implicit val insertUrlRequestValidator: Validator[InsertUrlRequest] = new Validator[InsertUrlRequest] {
    override def validate[B <: InsertUrlRequest](value: B): Either[List[String], B] =
      monadSequence(
        predicate[ValidationException](value.url.trim.nonEmpty, "url cannot be empty")
      )
      .map(_ => value)
  }
}