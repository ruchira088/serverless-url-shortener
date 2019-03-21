package com.ruchij.lambda.requests

import scala.util.{Success, Try}

trait QueryParameterParser[+A] {
  def parse(value: String): Try[A]
}

object QueryParameterParser {
  implicit object StringQueryParameter extends QueryParameterParser[String] {
    override def parse(value: String): Try[String] = Success(value)
  }

  implicit object IntegerQueryParameter extends QueryParameterParser[Int] {
    override def parse(value: String): Try[Int] = Try(value.toInt)
  }
}
