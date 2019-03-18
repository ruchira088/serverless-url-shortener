package modules
import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.dao.{RedisUrlDao, UrlDao}
import com.ruchij.config.redis.RedisConfiguration
import com.ruchij.configuration.ConfigLoader
import javax.inject.Singleton
import play.api.Configuration
import redis.RedisClient

class RedisModule extends AbstractModule {
  @Provides
  @Singleton
  def redisClient(implicit actorSystem: ActorSystem, configuration: Configuration): RedisClient = {
    val redisConfiguration = ConfigLoader.parse[RedisConfiguration](configuration.underlying).get
    RedisClient(host = redisConfiguration.host, port = redisConfiguration.port, password = redisConfiguration.password)
  }

  @Provides
  @Singleton
  def redisUrlDao(redisClient: RedisClient): UrlDao =
    new RedisUrlDao(redisClient)
}
