package com.ruchij.lambda.handlers
import com.ruchij.exceptions.ValidationException

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object HandlerUtils {
  sealed trait PathPrefix {
    self =>

    val path: String
    lazy val regex: Regex = keyRegex(self)
  }

  case object InfoPrefix extends PathPrefix {
    override val path: String = "url"
  }

  case object RedirectPrefix extends PathPrefix {
    override val path: String = "redirect"
  }

  case object DeleteSecretPrefix extends PathPrefix {
    override val path: String = "delete-secret"
  }

  def keyRegex(pathPrefix: PathPrefix): Regex = s"""\\/${pathPrefix.path}\\/([A-Za-z0-9_\\-\\.\\+]+)""".r

  def extractKey(pathPrefix: PathPrefix): PartialFunction[String, Try[String]] = {
    case pathPrefix.regex(key) => Success(key)
    case path =>
      Failure(ValidationException(s"""Unable to extract "${pathPrefix.path}" from $path"""))
  }
}
