package modules

import com.google.inject.{AbstractModule, Provides}
import com.ruchij.config.service.ServiceConfiguration
import com.ruchij.configuration.ConfigLoader
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.providers.Providers
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import ec.BlockingExecutionContextImpl
import javax.inject.Singleton
import play.api.Configuration
import play.api.libs.json.Json

class CoreModule extends AbstractModule {

  override def configure(): Unit = {
    println {
      s"""
        |Environment variables:
        |${Json.prettyPrint(Json.toJson(sys.env))}
      """.stripMargin
    }

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
    bind(classOf[Providers]).toInstance(Providers)
  }

  @Provides
  @Singleton
  def serviceConfiguration(implicit configuration: Configuration): ServiceConfiguration = {
    val serviceConfig = ConfigLoader.parse[ServiceConfiguration](configuration.underlying).get

    println {
      s"""
        |Service configuration:
        |${Json.prettyPrint(Json.toJson(serviceConfig))}
      """.stripMargin
    }

    serviceConfig
  }
}
