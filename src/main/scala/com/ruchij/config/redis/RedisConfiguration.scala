package com.ruchij.config.redis

import scala.language.implicitConversions

case class RedisConfiguration(host: String, port: Int, password: Option[String])
