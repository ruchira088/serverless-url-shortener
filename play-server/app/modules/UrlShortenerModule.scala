package modules
import com.google.inject.AbstractModule
import com.ruchij.dao.UrlDao
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import com.ruchij.services.url.models.ServiceConfiguration
import config.EnvironmentVariables.envValueAs
import config.{EnvNames, EnvironmentVariables}
import dao.PlaySlickUrlDao
import ec.BlockingExecutionContextImpl
import play.api.libs.json.Json

import scala.util.Try

class UrlShortenerModule extends AbstractModule {
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
    bind(classOf[UrlDao]).to(classOf[PlaySlickUrlDao])
//    bind(classOf[UrlDao]).to(classOf[InMemoryUrlDao])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
    bind(classOf[ServiceConfiguration]).toInstance(configuration)
  }

  def serviceConfiguration(implicit environmentVariables: EnvironmentVariables): Try[ServiceConfiguration] =
    for {
      keySize <- envValueAs[Int](EnvNames.KEY_SIZE, ServiceConfiguration.default.keyLength)
      fixedKeyLengthRetries <- envValueAs[Int](
        EnvNames.FIXED_KEY_LENGTH_RETRIES,
        ServiceConfiguration.default.fixedKeyLengthRetries
      )
    } yield ServiceConfiguration(keySize, fixedKeyLengthRetries)
}
