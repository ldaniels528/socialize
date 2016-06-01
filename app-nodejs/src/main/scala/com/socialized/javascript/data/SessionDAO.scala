package com.socialized.javascript.data

import com.github.ldaniels528.meansjs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Session DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait SessionDAO extends Collection

/**
  * Session DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object SessionDAO {

  /**
    * Session DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class SessionDAOExtensions(val db: Db) extends AnyVal {

    def getSessionDAO(implicit ec: ExecutionContext) = db.collectionFuture("sessions").map(_.asInstanceOf[SessionDAO])

  }

}