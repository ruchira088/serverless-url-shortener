package com.ruchij.providers

import org.joda.time.DateTime

trait TimestampProvider {
  def dateTime(): DateTime = DateTime.now()
}
