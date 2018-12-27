package com.ruchij.services
import com.ruchij.FutureOpt

import scala.concurrent.ExecutionContext

trait UrlShorteningService {
  def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url]

//  def insert(longUrl: String)(implicit executionContext: ExecutionContext): F
}
