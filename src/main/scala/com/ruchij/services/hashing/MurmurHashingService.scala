package com.ruchij.services.hashing
import com.ruchij.ec.BlockingExecutionContext
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.hashing.MurmurHash3

@Singleton
class MurmurHashingService @Inject()(implicit blockingExecutionContext: BlockingExecutionContext) extends HashingService {
  override def hash[A](value: A)(implicit stringifier: Stringifier[A], executionContext: ExecutionContext): Future[String] =
    Future {
      HashingService.calculateString(MurmurHash3.stringHash(stringifier.stringify(value)))
    }(blockingExecutionContext)
}