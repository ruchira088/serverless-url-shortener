package com.ruchij.lambda.models
import com.ruchij.exceptions.MissingPathParameter

import scala.util.{Failure, Try}

trait PathParameter[A] {
  val key: String
}

object PathParameter {
  def pathParameter[A](pathParameter: PathParameter[A], request: Request)(implicit transformer: Transformer[String, A]): Try[A] =
    request.pathParameters.get(pathParameter.key)
      .fold[Try[A]] { Failure(MissingPathParameter(pathParameter.key)) } {
        transformer.transform
      }

  case object UrlKey extends PathParameter[String] {
    override val key: String = "key"
  }
}
