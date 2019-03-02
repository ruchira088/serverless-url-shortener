package com.ruchij.monad
import scala.language.higherKinds

case class FoldableMonadInMonad[+A, M[+ _], FM[+_]](value: M[FM[A]]) {
  def flatMap[B](f: A => FoldableMonadInMonad[B, M, FM])(implicit monad: Monad[M], foldableMonad: FoldableMonad[FM]): FoldableMonadInMonad[B, M, FM] =
    FoldableMonadInMonad {
      monad.flatMap {
        foldableMonad.fold[A, M[FM[B]]](throwable => monad.lift(foldableMonad.failure(throwable)))(a => f(a).value)
      }(value)
    }

  def flatMapM[B](f: A => M[B])(implicit monad: Monad[M], foldableMonad: FoldableMonad[FM]): FoldableMonadInMonad[B, M, FM] =
    flatMap {
      result => FoldableMonadInMonad {
        monad.map[B, FM[B]](foldableMonad.lift)(f(result))
      }
    }

  def flatMapFM[B](f: FM[A] => M[B])(implicit monad: Monad[M], functor: Functor[FM]): FoldableMonadInMonad[B, M, FM] =
  FoldableMonadInMonad {
    monad.map[B, FM[B]](functor.lift)(monad.flatMap(f)(value))
  }

  def mapF[B](f: A => FM[B])(implicit monad: Monad[M], foldableMonad: FoldableMonad[FM]): FoldableMonadInMonad[B, M, FM] =
    flatMap {
      result => FoldableMonadInMonad { monad.lift(f(result)) }
    }

  def map[B](f: A => B)(implicit functorM: Functor[M], functorFM: Functor[FM]): FoldableMonadInMonad[B, M, FM] =
    FoldableMonadInMonad {
      functorM.map(functorFM.map(f))(value)
    }

  def flatten(implicit monad: Monad[M] { type Failure = Throwable }, foldableMonad: FoldableMonad[FM]): M[A] =
    monad.flatMap(foldableMonad.fold[A, M[A]](failure => monad.failure(foldableMonad.throwable(failure)))(monad.lift))(value)
}
