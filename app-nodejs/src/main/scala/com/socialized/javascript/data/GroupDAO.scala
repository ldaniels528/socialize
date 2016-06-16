package com.socialized.javascript.data

import org.scalajs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Group DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait GroupDAO extends Collection

/**
  * Group DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object GroupDAO {

  /**
    * Group DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class GroupDAOExtensions(val db: Db) extends AnyVal {

    def getGroupDAO(implicit ec: ExecutionContext) = db.collectionFuture("groups").map(_.asInstanceOf[GroupDAO])

  }

}