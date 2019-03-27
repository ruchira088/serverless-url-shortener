package com.ruchij.dao.models
import play.api.libs.json.{JsString, Writes}

trait InitializationResult {
  def message: String
}

object InitializationResult {
  implicit val initializationResultWrite: Writes[InitializationResult] =
    (initializationResult: InitializationResult) => JsString(initializationResult.message)

  case class SlickDaoInitializationResult(tableName: String) extends InitializationResult {
    override def message: String = s"""Successfully created "$tableName" table"""
  }
}
