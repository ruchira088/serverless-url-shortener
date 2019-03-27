package com.ruchij.lambda.handlers
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object HandlerUtils {
  def await[A](future: Future[A]): A = Await.result(future, Duration.Inf)
}
