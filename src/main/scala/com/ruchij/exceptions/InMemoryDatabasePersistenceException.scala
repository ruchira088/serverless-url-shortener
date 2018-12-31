package com.ruchij.exceptions

case class InMemoryDatabasePersistenceException[A](value: A) extends Exception {
  override def getMessage: String = s"Unable to persist $value in the in-memory database"
}
