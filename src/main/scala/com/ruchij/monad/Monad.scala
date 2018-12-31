package com.ruchij.monad
import com.ruchij.exceptions.ErrorBuilder

import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

sealed trait Monad[M[+ _]] {
  type Failure

  def flatMap[A, B](f: A => M[B])(value: M[A]): M[B]

  def map[A, B](f: A => B)(value: M[A]): M[B] =
    flatMap[A, B](a => lift(f(a)))(value)

  def lift[A](value: A): M[A]

  def fold[A, B](failure: Failure => B)(success: A => B)(value: M[A]): B

  def failure(fail: Failure): M[Nothing]

  def failure(value: M[_]): Option[String]
}

object Monad {
  implicit object OptionMonad extends Monad[Option] {
    override type Failure = Unit

    override def flatMap[A, B](f: A => Option[B])(value: Option[A]): Option[B] = value.flatMap(f)

    override def lift[A](value: A): Option[A] = Option(value)

    override def fold[A, B](failure: Unit => B)(success: A => B)(value: Option[A]): B =
      value.fold(failure((): Unit))(success)

    override def failure(failure: Unit): Option[Nothing] = None

    override def failure(value: Option[_]): Option[String] =
      if (value.isEmpty) Some("Empty Option") else None
  }

  implicit object TryMonad extends Monad[Try] {
    override type Failure = Throwable

    override def flatMap[A, B](f: A => Try[B])(value: Try[A]): Try[B] = value.flatMap(f)

    override def lift[A](value: A): Try[A] = Try(value)

    override def fold[A, B](failure: Throwable => B)(success: A => B)(value: Try[A]): B =
      value.fold(failure, success)

    override def failure(fail: Throwable): Try[Nothing] = Failure(fail)

    override def failure(value: Try[_]): Option[String] = value.failed.map(_.getMessage).toOption

    def predicate[A <: Throwable](condition: Boolean, errorMessage: String)(implicit errorBuilder: ErrorBuilder[A]): Try[_] =
      if (condition) Success((): Unit) else Failure(errorBuilder.error(errorMessage))
  }

  def monadSequence[A, M[+ _], Error <: Throwable](values: M[A]*)(implicit monad: Monad[M]): Either[List[String], List[A]] =
    values.toList match {
      case Nil => Right(List.empty)
      case list @ x :: xs =>
        monad.fold[A, Either[List[String], List[A]]](_ => Left(monadFailures(list))) { result =>
          monadSequence(xs: _*).map(result :: _)
        }(x)
    }

  private def monadFailures[M[+ _]](values: List[M[_]])(implicit monad: Monad[M]): List[String] =
    values match {
      case Nil => List.empty
      case x :: xs => monad.failure(x).toList ++ monadFailures(xs)
    }

}
