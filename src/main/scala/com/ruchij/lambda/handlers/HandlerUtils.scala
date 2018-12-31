package com.ruchij.lambda.handlers
import com.ruchij.exceptions.ValidationException

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

object HandlerUtils {
  sealed trait PathPrefix {
    def path: String
  }

  case object InfoPrefix extends PathPrefix {
    override def path: String = "url"
  }

  case object RedirectPrefix extends PathPrefix {
    override def path: String = "key"
  }

  def keyRegex(pathPrefix: PathPrefix): Regex = s"""\\/${pathPrefix.path}\\/([A-Za-z0-9_\\-\\.\\+]+)""".r

  def extractKey(regex: Regex): PartialFunction[String, Try[String]] = {
    case regex(key) => Success(key)
    case path => Failure(ValidationException(s"Unable to extract key from $path"))
  }
}
