package config

import org.apache.commons.lang3.StringUtils
import play.api.libs.json.{Json, OWrites}

import scala.util.{Success, Try}

case class EnvironmentVariables(values: Map[String, String])

object EnvironmentVariables {
  val MASKED_KEYS: List[String] = List("POSTGRES_PASSWORD", "MYSQL_PASSWORD")

  implicit val environmentVariablesWrites: OWrites[EnvironmentVariables] =
    (environmentVariables: EnvironmentVariables) =>
      Json.toJsObject {
        environmentVariables.values
          .map {
            case (key, value) => key -> (if (MASKED_KEYS.contains(key)) mask(value) else value)
          }
      }

  def mask(value: String, result: String = StringUtils.EMPTY): String =
    value.toList match {
      case Nil => result
      case x :: y :: z if z.length > 4 && result.isEmpty => mask(z.mkString, s"$x$y")
      case x :: y :: Nil if result.length > 4 => s"$result$x$y"
      case _ :: xs => mask(xs.mkString, s"$result*")
    }

  def envValue(name: String)(implicit environmentVariables: EnvironmentVariables): Option[String] =
    environmentVariables.values.get(name)

  def envValueAs[A](
    name: String,
    default: A
  )(implicit envValueMapper: EnvValueMapper[A], environmentVariables: EnvironmentVariables): Try[A] =
    envValue(name).fold[Try[A]](Success(default))(envValueMapper.value)
}
