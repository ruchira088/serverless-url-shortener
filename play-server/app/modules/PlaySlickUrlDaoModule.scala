package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.dao.SlickUrlDao
import javax.inject.Singleton
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class PlaySlickUrlDaoModule extends AbstractModule {

  @Provides
  @Singleton
  def slickUrlDao(databaseConfigProvider: DatabaseConfigProvider): SlickUrlDao = {
    val provider: DatabaseConfig[JdbcProfile] = databaseConfigProvider.get
    new SlickUrlDao(provider.profile, provider.db)
  }
}
