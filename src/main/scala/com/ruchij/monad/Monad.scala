package com.ruchij.monad
import scala.language.higherKinds

sealed trait Monad[M[+ _]] {
  type Failure

  def flatMap[A, B](f: A => M[B])(value: M[A]): M[B]

  def map[A, B](f: A => B)(value: M[A]): M[B] =
    flatMap[A, B](a => lift(f(a)))(value)

  def lift[A](value: A): M[A]

  def fold[A, B](failure: Failure => B)(success: A => B)(value: M[A]): B

  def failure(fail: Failure): M[Nothing]
}

object Monad {
  implicit object OptionMonad extends Monad[Option] {
    override type Failure = Unit

    override def flatMap[A, B](f: A => Option[B])(value: Option[A]): Option[B] = value.flatMap(f)

    override def lift[A](value: A): Option[A] = Option(value)

    override def fold[A, B](failure: Unit => B)(success: A => B)(value: Option[A]): B =
      value.fold(failure((): Unit))(success)

    override def failure(failure: Unit): Option[Nothing] = None
  }
}