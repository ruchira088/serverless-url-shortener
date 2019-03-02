package com.ruchij.monad
import scala.language.higherKinds

trait Functor[F[+ _]] {
  def lift[A](value: A): F[A]

  def map[A, B](f: A => B)(value: F[A]): F[B]
}
