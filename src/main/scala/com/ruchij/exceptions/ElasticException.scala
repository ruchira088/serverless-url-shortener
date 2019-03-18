package com.ruchij.exceptions
import com.sksamuel.elastic4s.http.ElasticError

case class ElasticException(elasticError: ElasticError) extends Exception
