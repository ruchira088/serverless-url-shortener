package com.ruchij.exceptions

case class ExistingUrlKeyException(urlKey: String) extends Exception {
  override def getMessage: String = s"$urlKey ALREADY exists"
}
