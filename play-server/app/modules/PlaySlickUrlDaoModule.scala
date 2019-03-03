package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.dao.{SlickUrlDao, UrlDao}
import dao.PlaySlickUrlDao
import javax.inject.Singleton
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class PlaySlickUrlDaoModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UrlDao]).to(classOf[PlaySlickUrlDao])
  }

  @Provides
  @Singleton
  def slickUrlDao(databaseConfigProvider: DatabaseConfigProvider): SlickUrlDao = {
    val provider: DatabaseConfig[JdbcProfile] = databaseConfigProvider.get
    new SlickUrlDao(provider.profile, provider.db)
  }
}
