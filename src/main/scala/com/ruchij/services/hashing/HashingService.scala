package com.ruchij.services.hashing

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

trait HashingService {
  def hash[A](value: A)(implicit stringifier: Stringifier[A], executionContext: ExecutionContext): Future[String]
}

object HashingService {
  val VALUES: Array[String] = ('1' to 'z' toArray).filter(_.isLetterOrDigit).map(_.toString)

  def calculateString(int: Int): String =
    if (int < VALUES.length)
      VALUES(int)
    else
      VALUES(int % VALUES.length) + calculateString(int / VALUES.length)
}