package com.socialized.dao

import com.socialized.dao.mongodb.SessionDAOMongoDB
import com.socialized.models.UserSession
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Represents a Session DAO
  * @author lawrence.daniels@gmail.com
  */
trait SessionDAO {

  def update(session: UserSession)(implicit ec: ExecutionContext): Future[Option[UserSession]]

}

/**
  * Session DAO
  * @author lawrence.daniels@gmail.com
  */
object SessionDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new SessionDAOMongoDB(reactiveMongoApi)
  }

}