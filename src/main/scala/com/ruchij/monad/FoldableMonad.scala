package com.ruchij.monad

import com.ruchij.exceptions.{EmptyOptionException, ErrorBuilder}

import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait FoldableMonad[F[+ _]] extends Monad[F] {
  def fold[A, B](onFailure: Failure => B)(onSuccess: A => B)(value: F[A]): B

  def throwable(failure: Failure): Throwable

  def failureMessage[A](value: F[A]): Option[String] =
    fold[A, Option[Throwable]](failure => Some(throwable(failure)))(_ => None)(value).map(_.getMessage)
}

object FoldableMonad {
  implicit object OptionMonad extends FoldableMonad[Option] {
    override type Failure = Unit

    override def flatMap[A, B](f: A => Option[B])(value: Option[A]): Option[B] = value.flatMap(f)

    override def lift[A](value: A): Option[A] = Option(value)

    override def fold[A, B](failure: Unit => B)(success: A => B)(value: Option[A]): B =
      value.fold(failure((): Unit))(success)

    override def failure(failure: Unit): Option[Nothing] = None

    override def throwable(failure: Unit): Throwable = EmptyOptionException
  }

  implicit object TryMonad extends FoldableMonad[Try] {
    override type Failure = Throwable

    override def flatMap[A, B](f: A => Try[B])(value: Try[A]): Try[B] = value.flatMap(f)

    override def lift[A](value: A): Try[A] = Try(value)

    override def fold[A, B](failure: Throwable => B)(success: A => B)(value: Try[A]): B =
      value.fold(failure, success)

    override def failure(fail: Throwable): Try[Nothing] = Failure(fail)

    override def throwable(failure: Throwable): Throwable = failure

    def predicate[A <: Throwable](condition: Boolean, errorMessage: String)(implicit errorBuilder: ErrorBuilder[A]): Try[_] =
      if (condition) Success((): Unit) else Failure(errorBuilder.error(errorMessage))
  }

  def foldableMonadSequence[A, F[+ _], Error <: Throwable](values: F[A]*)(implicit foldableMonad: FoldableMonad[F]): Either[List[String], List[A]] =
    values.toList match {
      case Nil => Right(List.empty)
      case list @ x :: xs =>
        foldableMonad.fold[A, Either[List[String], List[A]]](_ => Left(foldableMonadFailures(list))) { result =>
          foldableMonadSequence(xs: _*).map(result :: _)
        }(x)
    }

  private def foldableMonadFailures[F[+ _]](values: List[F[_]])(implicit foldableMonad: FoldableMonad[F]): List[String] =
    values match {
      case Nil => List.empty
      case x :: xs => foldableMonad.failureMessage(x).toList ++ foldableMonadFailures(xs)
    }
}
