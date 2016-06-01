package com.socialized.javascript.data

import com.github.ldaniels528.meansjs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Credential DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait CredentialDAO extends Collection

/**
  * Credential DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object CredentialDAO {

  /**
    * Credential DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class CredentialDAOExtensions(val db: Db) extends AnyVal {

    def getCredentialDAO(implicit ec: ExecutionContext) = db.collectionFuture("credentials").map(_.asInstanceOf[CredentialDAO])

  }

}