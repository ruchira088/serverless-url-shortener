package com.ruchij.services.url
import com.ruchij.FutureOpt
import com.ruchij.services.url.models.Url

import scala.concurrent.{ExecutionContext, Future}

trait UrlShorteningService {
  def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url]

  def insert(longUrl: String)(implicit executionContext: ExecutionContext): Future[Either[Url, Url]]
}
