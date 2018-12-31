package com.ruchij.exceptions

case class MissingUrlKeyException(key: String) extends Exception {
  override def getMessage: String = s"Unable to find key: $key"
}
