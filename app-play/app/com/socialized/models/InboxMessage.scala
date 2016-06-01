package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{BSONObjectID, Macros}

/**
 * Represents an Inbox Message model object
 * @author lawrence.daniels@gmail.com
 */
case class InboxMessage(_id: Option[BSONObjectID] = BSONObjectID.generate,
                        title: Option[String],
                        message: Option[String],
                        creationTime: Option[Date] = new Date())

/**
 * Inbox Message Companion
 * @author lawrence.daniels@gmail.com
 */
object InboxMessage {

  implicit val InboxMessageHandler = Macros.handler[InboxMessage]

}
