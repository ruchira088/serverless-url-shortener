package com.ruchij.monad
import scala.language.higherKinds

trait Monad[M[+ _]] extends Functor[M] {
  type Failure

  def flatMap[A, B](f: A => M[B])(value: M[A]): M[B]

  def map[A, B](f: A => B)(value: M[A]): M[B] =
    flatMap[A, B](a => lift(f(a)))(value)

  def failure(fail: Failure): M[Nothing]
}
