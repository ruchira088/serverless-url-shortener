package com.ruchij.monad
import scala.language.higherKinds

sealed trait Monad[M[+ _]] {
  def flatMap[A, B](f: A => M[B])(value: M[A]): M[B]

  def map[A, B](f: A => B)(value: M[A]): M[B] =
    flatMap[A, B](a => lift(f(a)))(value)

  def lift[A](value: A): M[A]

  def fold[A, B](success: A => B)(failure: Throwable => B)(value: M[A]): B

  def failure(throwable: Throwable): M[Nothing]
}

object Monad {
  implicit object OptionMonad extends Monad[Option] {
    override def flatMap[A, B](f: A => Option[B])(value: Option[A]): Option[B] = value.flatMap(f)

    override def lift[A](value: A): Option[A] = Option(value)

    override def fold[A, B](success: A => B)(failure: Throwable => B)(value: Option[A]): B = ???

    override def failure(throwable: Throwable): Option[Nothing] = None
  }
}