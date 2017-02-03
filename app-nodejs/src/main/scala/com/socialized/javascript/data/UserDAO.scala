package com.socialized.javascript.data

import io.scalajs.npm.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * User DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait UserDAO extends Collection

/**
  * User DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object UserDAO {

  /**
    * User DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class UserDAOExtensions(val db: Db) extends AnyVal {

    def getUserDAO(implicit ec: ExecutionContext) = db.collectionFuture("users").map(_.asInstanceOf[UserDAO])

  }

}
