package com.socialized.models

import java.util.Date

import com.socialized.models.PermissionTypes.PermissionType
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{BSONObjectID, Macros}

/**
  * Represents an event model object
  * @author lawrence.daniels@gmail.com
  */
case class Event(_id: Option[BSONObjectID] = BSONObjectID.generate,
                 `type`: Option[String],
                 title: Option[String],
                 permissions: Option[PermissionType],
                 owner: Option[Submitter],
                 eventTime: Option[Date],
                 creationTime: Option[Date] = new Date())

/**
  * Event Companion Object
  * @author lawrence.daniels@gmail.com
  */
object Event {

  implicit val EventFormat = Json.format[Event]

  implicit val EventHandler = Macros.handler[Event]

}