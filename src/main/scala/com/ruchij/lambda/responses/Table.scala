package com.ruchij.lambda.responses

import play.api.libs.json.{Json, OFormat}
import slick.jdbc.meta.MTable

case class Table(name: String, tableType: String)

object Table {
  implicit val tableFormat: OFormat[Table] = Json.format[Table]

  def fromMTable(mTable: MTable): Table = Table(mTable.name.name, mTable.tableType)
}
