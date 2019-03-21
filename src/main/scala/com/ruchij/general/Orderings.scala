package com.ruchij.general
import org.joda.time.DateTime

object Orderings {
  implicit object DateTimeOrdering extends Ordering[DateTime] {
    override def compare(x: DateTime, y: DateTime): Int = (x.getMillis - y.getMillis).toInt
  }
}
