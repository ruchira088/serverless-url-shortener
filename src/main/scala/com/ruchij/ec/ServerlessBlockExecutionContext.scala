package com.ruchij.ec
import scala.concurrent.ExecutionContext

class ServerlessBlockExecutionContext()(implicit executionContext: ExecutionContext)
    extends BlockingExecutionContext {
  override def execute(runnable: Runnable): Unit = executionContext.execute(runnable)
  override def reportFailure(cause: Throwable): Unit = executionContext.reportFailure(cause)
}
