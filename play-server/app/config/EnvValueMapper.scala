package config

import scala.util.Try

trait EnvValueMapper[+A] {
  def value(string: String): Try[A]
}

object EnvValueMapper {
  implicit val IntEnvValueMapper: EnvValueMapper[Int] = (string: String) => Try(string.toInt)
}
