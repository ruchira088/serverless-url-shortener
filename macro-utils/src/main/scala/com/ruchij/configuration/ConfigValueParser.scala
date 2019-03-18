package com.ruchij.configuration

import com.typesafe.config.Config

import scala.util.{Success, Try}

trait ConfigValueParser[+A] {
  def getValue(path: String, config: Config): Try[A]
}

object ConfigValueParser {
  def getValue[A](path: String, config: Config)(implicit configValueParser: ConfigValueParser[A]): Try[A] =
    configValueParser.getValue(path, config)

  implicit val intConfigValueParser: ConfigValueParser[Int] =
    (path: String, config: Config) => Try(config.getInt(path))

  implicit val stringConfigValueParser: ConfigValueParser[String] =
    (path: String, config: Config) => Try(config.getString(path))

  implicit val booleanConfigValueParser: ConfigValueParser[Boolean] =
    (path: String, config: Config) => Try(config.getBoolean(path))

  implicit def optionConfigValueParser[A](implicit configValueParser: ConfigValueParser[A]): ConfigValueParser[Option[A]] =
    (path: String, config: Config) =>
      if (config.hasPath(path)) configValueParser.getValue(path, config).map(Option.apply) else Success(None)
}
