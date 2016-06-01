package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{BSONObjectID, Macros}

/**
  * Represents a notification model object
  * @author lawrence.daniels@gmail.com
  */
case class Notification(_id: Option[BSONObjectID] = BSONObjectID.generate,
                        `type`: Option[String],
                        title: Option[String],
                        text: Option[String],
                        owner: Option[Submitter],
                        eventTime: Option[Date],
                        readTime: Option[Date],
                        creationTime: Option[Date] = new Date())

/**
  * Notification Companion Object
  * @author lawrence.daniels@gmail.com
  */
object Notification {

  implicit val NotificationFormat = Json.format[Notification]

  implicit val NotificationHandler = Macros.handler[Notification]

}