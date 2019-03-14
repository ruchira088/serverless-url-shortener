package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.dao.ReactiveMongoUrlDao.MongoDatabase
import com.ruchij.dao.{ReactiveMongoUrlDao, UrlDao}
import javax.inject.Singleton
import play.modules.reactivemongo.ReactiveMongoApi

class ReactiveMongoUrlDaoModule extends AbstractModule {
  @Provides
  @Singleton
  def reactiveMongo(reactiveMongoApi: ReactiveMongoApi): UrlDao =
    new ReactiveMongoUrlDao(MongoDatabase(reactiveMongoApi.database))
}
