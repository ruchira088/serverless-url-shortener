package com.ruchij.services.hashing
import scala.concurrent.{ExecutionContext, Future}

trait HashingService {
  def hash[A](value: A)(implicit stringifier: Stringifier[A], executionContext: ExecutionContext): Future[String]
}