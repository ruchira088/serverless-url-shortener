package com.ruchij.lambda.requests

trait Validator[-A] {
  def validate[B <: A](value: B): Either[List[String], B]
}
