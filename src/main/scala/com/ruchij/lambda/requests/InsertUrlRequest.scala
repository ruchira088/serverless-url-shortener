package com.ruchij.lambda.requests

import com.ruchij.exceptions.ValidationException
import com.ruchij.monad.FoldableMonad.foldableMonadSequence
import com.ruchij.monad.FoldableMonad.TryMonad.predicate
import play.api.libs.json.{Json, OFormat}

case class InsertUrlRequest(url: String, key: Option[String])

object InsertUrlRequest  {
  implicit val insertUrlRequestFormat: OFormat[InsertUrlRequest] = Json.format[InsertUrlRequest]

  implicit val insertUrlRequestValidator: Validator[InsertUrlRequest] = new Validator[InsertUrlRequest] {
    override def validate[B <: InsertUrlRequest](value: B): Either[List[String], B] =
      foldableMonadSequence(
        predicate[ValidationException](value.url.trim.nonEmpty, "url cannot be empty")
      )
      .map(_ => value)
  }
}
