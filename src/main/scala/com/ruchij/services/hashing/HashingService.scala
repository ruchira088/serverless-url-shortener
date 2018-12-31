package com.ruchij.services.hashing

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

trait HashingService {
  def hash[A](value: A)(implicit stringifier: Stringifier[A], executionContext: ExecutionContext): Future[String]
}

object HashingService {
  val VALUES: Array[String] = ('1' to 'z' toArray).filter(_.isLetterOrDigit).map(_.toString)

  def calculateString(int: Int): String = {
    val absInt = math.abs(int)

    if (absInt < VALUES.length)
      VALUES(absInt)
    else
      VALUES(absInt % VALUES.length) + calculateString(absInt / VALUES.length)
  }
}