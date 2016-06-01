package com.socialized.dao.mongodb

import com.socialized.dao.CredentialDAO
import com.socialized.models.Credential
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

/**
  * Credential DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class CredentialDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends CredentialDAO with EntityMaintenanceDAO[Credential] {

  override val collectionName = "credentials"

  override def findByUsername(screenName: String)(implicit ec: ExecutionContext) = {
    findOneBy("username" -> screenName)
  }

  override def update(credential: Credential)(implicit ec: ExecutionContext) = {
    replace(credential._id, credential)
  }

}
