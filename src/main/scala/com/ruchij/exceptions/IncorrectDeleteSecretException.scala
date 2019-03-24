package com.ruchij.exceptions

object IncorrectDeleteSecretException extends Exception {
  override def getMessage: String = "Incorrect delete-secret for URL key"
}
