package com.socialized.javascript.data

import org.scalajs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Post DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait PostDAO extends Collection

/**
  * Post DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object PostDAO {

  /**
    * Post DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class PostDAOExtensions(val db: Db) extends AnyVal {

    def getPostDAO(implicit ec: ExecutionContext) = db.collectionFuture("posts").map(_.asInstanceOf[PostDAO])

  }

}
