package com.ruchij.exceptions

case object NonExistentRequestBodyException extends Exception {
  override def getMessage: String = "Request does not contain a body"
}
