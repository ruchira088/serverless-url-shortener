package modules
import com.google.inject.Provides
import com.ruchij.dao.{SlickUrlDao, UrlDao}
import dao.PlaySlickUrlDao
import javax.inject.Singleton
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class PlaySlickUrlDaoModule extends CoreModule {

  @Provides
  @Singleton
  def slickUrlDao(databaseConfigProvider: DatabaseConfigProvider): SlickUrlDao = {
    val provider: DatabaseConfig[JdbcProfile] = databaseConfigProvider.get
    new SlickUrlDao(provider.profile, provider.db)
  }

  override def databaseBinding(): Unit =
    bind(classOf[UrlDao]).to(classOf[PlaySlickUrlDao])
}
