package com.ruchij.exceptions

case class DatabaseException(error: String) extends Exception {
  override def getMessage: String = error
}

object DatabaseException {
  implicit val databaseExceptionErrorBuilder: ErrorBuilder[DatabaseException] =
    (message: String) => DatabaseException(message)
}
