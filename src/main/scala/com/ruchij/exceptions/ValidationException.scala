package com.ruchij.exceptions

case class ValidationException(description: String) extends Exception {
  override def getMessage: String = description
}

object ValidationException {
  implicit val validationExceptionErrorBuilder: ErrorBuilder[ValidationException] =
    errorMessage => ValidationException(errorMessage)
}
