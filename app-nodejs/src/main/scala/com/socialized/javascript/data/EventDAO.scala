package com.socialized.javascript.data

import org.scalajs.nodejs.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Event DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait EventDAO extends Collection

/**
  * Event DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object EventDAO {

  /**
    * Event DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class EventDAOExtensions(val db: Db) extends AnyVal {

    def getEventDAO(implicit ec: ExecutionContext) = db.collectionFuture("events").map(_.asInstanceOf[EventDAO])

  }

}