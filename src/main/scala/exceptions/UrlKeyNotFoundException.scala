package exceptions

case class UrlKeyNotFoundException(key: String) extends Exception {
  override def getMessage: String = s"Unable to find key: $key"
}
