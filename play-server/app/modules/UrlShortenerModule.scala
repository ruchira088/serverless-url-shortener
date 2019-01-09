package modules
import com.google.inject.AbstractModule
import com.ruchij.dao.{InMemoryUrlDao, UrlDao}
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.services.hashing.{HashingService, MurmurHashingService}
import dao.PlaySlickUrlDao
import ec.BlockingExecutionContextImpl

class UrlShortenerModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])
    bind(classOf[UrlDao]).to(classOf[PlaySlickUrlDao])
//    bind(classOf[UrlDao]).to(classOf[InMemoryUrlDao])
    bind(classOf[HashingService]).to(classOf[MurmurHashingService])
  }
}