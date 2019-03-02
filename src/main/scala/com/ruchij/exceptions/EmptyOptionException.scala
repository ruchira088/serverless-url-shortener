package com.ruchij.exceptions

case object EmptyOptionException extends Exception {
  override def getMessage: String = "Empty Option"
}
