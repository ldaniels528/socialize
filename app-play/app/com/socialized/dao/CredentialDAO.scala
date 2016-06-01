package com.socialized.dao

import com.socialized.dao.mongodb.CredentialDAOMongoDB
import com.socialized.models.Credential
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Represents an Authentication Credential DAO
  * @author lawrence.daniels@gmail.com
  */
trait CredentialDAO {

  def findByUsername(screenName: String)(implicit ec: ExecutionContext): Future[Option[Credential]]

  def update(credential: Credential)(implicit ec: ExecutionContext): Future[Option[Credential]]

}

/**
  * Authentication Credential DAO implementation
  * @author lawrence.daniels@gmail.com
  */
object CredentialDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new CredentialDAOMongoDB(reactiveMongoApi)
  }

}