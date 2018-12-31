package ec
import akka.actor.ActorSystem
import com.ruchij.ec.BlockingExecutionContext
import ec.BlockingExecutionContextImpl.CONTEXT_NAME
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

class BlockingExecutionContextImpl @Inject()(implicit actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, CONTEXT_NAME)
    with BlockingExecutionContext

object BlockingExecutionContextImpl {
  val CONTEXT_NAME = "blocking-execution-context"
}