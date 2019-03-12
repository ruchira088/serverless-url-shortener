package com
import com.ruchij.monad.FoldableMonadInMonad

import scala.concurrent.Future

package object ruchij {
  type FutureOpt[A] = FoldableMonadInMonad[A, Future, Option]

  val FutureOpt: FoldableMonadInMonad.type = FoldableMonadInMonad
}
