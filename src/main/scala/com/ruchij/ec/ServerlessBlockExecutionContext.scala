package com.ruchij.ec

class ServerlessBlockExecutionContext(implicit executionContext: BlockingExecutionContext)
    extends BlockingExecutionContext {
  override def execute(runnable: Runnable): Unit = executionContext.execute(runnable)
  override def reportFailure(cause: Throwable): Unit = executionContext.reportFailure(cause)
}
