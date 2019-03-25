package com.ruchij.exceptions

case class DatabaseTableInitializationException(tableName: String) extends Exception {
  override def getMessage: String = s"Unable to initialize table: $tableName"
}
