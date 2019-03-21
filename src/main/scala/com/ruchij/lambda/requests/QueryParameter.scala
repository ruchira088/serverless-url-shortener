package com.ruchij.lambda.requests
import com.ruchij.lambda.models.Request

import scala.util.{Success, Try}

trait QueryParameter[A] {
  val key: String

  def parse(request: Request, default: A)(implicit queryParameterParser: QueryParameterParser[A]): Try[A] =
    request.queryParameters.get(key).fold[Try[A]](Success(default)) {
      _.headOption.fold[Try[A]](Success(default))(queryParameterParser.parse)
    }
}

object QueryParameter {
  case object PageSize extends QueryParameter[Int] {
    override val key: String = "page-size"
  }

  case object Page extends QueryParameter[Int] {
    override val key: String = "page"
  }
}
