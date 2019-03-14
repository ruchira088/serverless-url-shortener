package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.config.ConfigLoader
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import com.ruchij.services.url.models.ServiceConfiguration
import config.EnvironmentVariables
import ec.BlockingExecutionContextImpl
import javax.inject.Singleton
import play.api.Configuration
import play.api.libs.json.Json

class CoreModule extends AbstractModule {

  override def configure(): Unit = {
    val environmentVariables: EnvironmentVariables = EnvironmentVariables(sys.env)

    println {
      s"""
        |Environment variables:
        |${Json.prettyPrint(Json.toJson(environmentVariables))}
      """.stripMargin
    }

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
  }

  @Provides
  @Singleton
  def serviceConfiguration(implicit configuration: Configuration): ServiceConfiguration = {
    val serviceConfig = ConfigLoader.parse[ServiceConfiguration](configuration.underlying).get

    println {
      s"""
        |Service configuration:
        |${Json.prettyPrint(Json.toJson(serviceConfiguration))}
      """.stripMargin
    }

    serviceConfig
  }

}
