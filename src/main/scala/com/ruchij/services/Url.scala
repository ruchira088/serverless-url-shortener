package com.ruchij.services

import org.joda.time.DateTime

case class Url(key: String, createdAt: DateTime, longUrl: String, hits: Double)