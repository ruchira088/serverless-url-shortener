package modules
import com.google.inject.AbstractModule
import com.ruchij.dao.{InMemoryUrlDao, UrlDao}
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import com.ruchij.services.url.models.ServiceConfiguration
import config.EnvironmentVariables.envValueAs
import config.{EnvNames, EnvironmentVariables}
import ec.BlockingExecutionContextImpl
import play.api.libs.json.Json

import scala.util.Try

class CoreModule extends AbstractModule {
  override def configure(): Unit = {
    implicit val environmentVariables: EnvironmentVariables = EnvironmentVariables(sys.env)
    val configuration = serviceConfiguration.get

    println {
      s"""
        |Environment variables:
        |${Json.prettyPrint(Json.toJson(environmentVariables))}
        |
        |Service configuration:
        |${Json.prettyPrint(Json.toJson(configuration))}
      """.stripMargin
    }

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
    bind(classOf[ServiceConfiguration]).toInstance(configuration)

    databaseBinding()
  }

  def databaseBinding(): Unit =
    bind(classOf[UrlDao]).to(classOf[InMemoryUrlDao])

  def serviceConfiguration(implicit environmentVariables: EnvironmentVariables): Try[ServiceConfiguration] =
    for {
      keySize <- envValueAs[Int](EnvNames.KEY_SIZE, ServiceConfiguration.default.keyLength)
      fixedKeyLengthRetries <- envValueAs[Int](
        EnvNames.FIXED_KEY_LENGTH_RETRIES,
        ServiceConfiguration.default.fixedKeyLengthRetries
      )
    } yield ServiceConfiguration(keySize, fixedKeyLengthRetries)
}
