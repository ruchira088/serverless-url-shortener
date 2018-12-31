package com.ruchij.exceptions

case class AggregatedValidationException(validationExceptions: Seq[ValidationException]) extends Exception {
  override def getMessage: String =
    s"Validation exceptions: [${validationExceptions.map(_.description).mkString(", ")}]"
}
