package com.socialized.dao.mongodb

import com.socialized.dao.SessionDAO
import com.socialized.models.UserSession
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

/**
  * Session DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class SessionDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends SessionDAO with EntityMaintenanceDAO[UserSession] {

  override val collectionName = "sessions"

  override def update(session: UserSession)(implicit ec: ExecutionContext) = {
    replace(session._id, session)
  }

}
