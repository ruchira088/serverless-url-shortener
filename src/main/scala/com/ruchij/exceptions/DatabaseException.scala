package com.ruchij.exceptions

case class DatabaseException(error: String) extends Exception {
  override def getMessage: String = error
}
