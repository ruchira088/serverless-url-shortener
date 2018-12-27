package com
import com.ruchij.monad.FutureM

package object ruchij {
  type FutureOpt[A] = FutureM[A, Option]
}