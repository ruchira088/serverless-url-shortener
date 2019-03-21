package com.ruchij.monad
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait Monad[M[+ _]] extends Functor[M] {
  type Failure

  def flatMap[A, B](f: A => M[B])(value: M[A]): M[B]

  def map[A, B](f: A => B)(value: M[A]): M[B] =
    flatMap[A, B](a => lift(f(a)))(value)

  def failure(fail: Failure): M[Nothing]

  def throwable(failure: Failure): Throwable
}

object Monad {
  implicit def futureMonad(implicit executionContext: ExecutionContext): Monad[Future] { type Failure = Throwable } =
    new Monad[Future] {
      override type Failure = Throwable

      override def flatMap[A, B](f: A => Future[B])(value: Future[A]): Future[B] = value.flatMap(f)

      override def failure(fail: Throwable): Future[Nothing] = Future.failed[Nothing](fail)

      override def lift[A](value: A): Future[A] = Future.successful(value)

      override def throwable(failure: Throwable): Throwable = failure
    }
}
