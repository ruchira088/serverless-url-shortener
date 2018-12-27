package com.ruchij.services.url.models
import org.joda.time.DateTime

case class Url(key: String, createdAt: DateTime, longUrl: String, hits: Double)
