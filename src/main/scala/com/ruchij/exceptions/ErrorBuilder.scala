package com.ruchij.exceptions

trait ErrorBuilder[A <: Throwable] {
  def error(message: String): A
}
