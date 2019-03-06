package modules
import com.google.inject.AbstractModule
import com.ruchij.config.ConfigLoader
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import com.ruchij.services.url.models.ServiceConfiguration
import com.typesafe.config.ConfigFactory
import config.EnvironmentVariables
import ec.BlockingExecutionContextImpl
import play.api.libs.json.Json

class CoreModule extends AbstractModule {

  override def configure(): Unit = {
    val environmentVariables: EnvironmentVariables = EnvironmentVariables(sys.env)
    val serviceConfiguration: ServiceConfiguration =
      ConfigLoader.parse[ServiceConfiguration](ConfigFactory.load()).get

    println {
      s"""
        |Environment variables:
        |${Json.prettyPrint(Json.toJson(environmentVariables))}
        |
        |Service configuration:
        |${Json.prettyPrint(Json.toJson(serviceConfiguration))}
      """.stripMargin
    }

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
    bind(classOf[ServiceConfiguration]).toInstance(serviceConfiguration)
  }
}
