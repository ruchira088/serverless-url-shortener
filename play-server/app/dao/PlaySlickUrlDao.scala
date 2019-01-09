package dao
import com.ruchij.FutureOpt
import com.ruchij.dao.{SlickUrlDao, UrlDao}
import com.ruchij.services.url.models.Url
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PlaySlickUrlDao @Inject()(slickUrlDao: SlickUrlDao) extends UrlDao {

  override def insert(url: Url)(implicit executionContext: ExecutionContext): Future[Url] =
    slickUrlDao.insert(url)

  override def fetch(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    slickUrlDao.fetch(key)

  override def incrementHit(key: String)(implicit executionContext: ExecutionContext): FutureOpt[Url] =
    slickUrlDao.incrementHit(key)
}
