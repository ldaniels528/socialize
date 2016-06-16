package com.socialized.javascript.data

import org.scalajs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Notification DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait NotificationDAO extends Collection

/**
  * Notification DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object NotificationDAO {

  /**
    * Notification DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class NotificationDAOExtensions(val db: Db) extends AnyVal {

    def getNotificationDAO(implicit ec: ExecutionContext) = db.collectionFuture("notifications").map(_.asInstanceOf[NotificationDAO])

  }

}