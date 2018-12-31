package com.ruchij.dao
import com.ruchij.FutureOpt
import com.ruchij.services.url.models.Url

import scala.concurrent.{ExecutionContext, Future}

trait UrlDao {
  def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url]

  def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url]

  def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url]
}
