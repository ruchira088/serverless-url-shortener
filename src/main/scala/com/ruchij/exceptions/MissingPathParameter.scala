package com.ruchij.exceptions

case class MissingPathParameter(name: String) extends Exception {
  override def getMessage: String = s"""Missing path parameter: "$name""""
}
