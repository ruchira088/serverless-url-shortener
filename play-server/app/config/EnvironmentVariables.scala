package config

import play.api.libs.json.{Json, OWrites}

import scala.util.{Success, Try}

case class EnvironmentVariables(values: Map[String, String])

object EnvironmentVariables {
  sealed trait EnvValueMapper[+A] {
    def value(string: String): Try[A]
  }

  implicit val IntEnvValueMapper: EnvValueMapper[Int] =
    new EnvValueMapper[Int] { override def value(string: String): Try[Int] = Try(string.toInt) }

  implicit val environmentVariablesWrites: OWrites[EnvironmentVariables] =
    (environmentVariables: EnvironmentVariables) => Json.toJsObject(environmentVariables.values)

  def envValue(name: String)(implicit environmentVariables: EnvironmentVariables): Option[String] =
    environmentVariables.values.get(name)

  def envValueAs[A](
    name: String,
    default: A
  )(implicit envValueMapper: EnvValueMapper[A], environmentVariables: EnvironmentVariables): Try[A] =
    envValue(name).fold[Try[A]](Success(default))(envValueMapper.value)
}
