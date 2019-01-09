package com.ruchij.monad
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

case class FutureM[+A, M[+_]](future: Future[M[A]]) {
  def flatMap[B](f: A => FutureM[B, M])(implicit monad: Monad[M], executionContext: ExecutionContext): FutureM[B, M] =
    FutureM {
      future.flatMap {
        monad.fold[A, Future[M[B]]](throwable => Future.successful(monad.failure(throwable)))(a => f(a).future)
      }
    }

  def flatMapF[B](f: A => Future[B])(implicit monad: Monad[M], executionContext: ExecutionContext): FutureM[B, M] =
    flatMap {
      value => FutureM { f(value).map(monad.lift) }
    }

  def flatMapM[B](f: M[A] => Future[B])(implicit monad: Monad[M], executionContext: ExecutionContext): FutureM[B, M] =
    FutureM {
      future.flatMap(f).map(monad.lift)
    }

  def map[B](f: A => B)(implicit monad: Monad[M], executionContext: ExecutionContext): FutureM[B, M] =
    flatMap { value => FutureM { Future.successful(monad.lift(f(value))) }}

  def flatten(throwable: Throwable)(implicit monad: Monad[M], executionContext: ExecutionContext): Future[A] =
    future.flatMap {
      monad.fold[A, Future[A]](_ => Future.failed(throwable))(Future.successful)
    }
}
