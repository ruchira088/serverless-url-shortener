package com.ruchij.lambda.models
import scala.util.{Success, Try}

trait Transformer[-A, +B] {
  def transform[C <: A](value: C): Try[B]
}

object Transformer {
  implicit object StringToStringTransformer extends Transformer[String, String] {
    override def transform[C <: String](value: C): Try[String] = Success(value)
  }
}
