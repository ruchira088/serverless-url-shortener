package modules
import com.google.inject.{AbstractModule, Provides}
import com.ruchij.dao.ReactiveMongoDao.MongoDatabase
import com.ruchij.dao.{ReactiveMongoDao, UrlDao}
import javax.inject.Singleton
import play.modules.reactivemongo.ReactiveMongoApi

class ReactiveMongoUrlDaoModule extends AbstractModule {
  @Provides
  @Singleton
  def reactiveMongo(reactiveMongoApi: ReactiveMongoApi): UrlDao =
    new ReactiveMongoDao(MongoDatabase(reactiveMongoApi.database))
}
