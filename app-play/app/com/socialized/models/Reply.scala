package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{BSONObjectID, Macros}

/**
  * Represents a reply
  * @author lawrence.daniels@gmail.com
  */
case class Reply(_id: Option[BSONObjectID] = BSONObjectID.generate,
                 text: Option[String],
                 submitter: Option[Submitter],
                 //likes: Option[Int],
                 //likedBy: Option[List[String]],
                 creationTime: Option[Date],
                 lastUpdateTime: Option[Date])

/**
  * Reply Companion
  * @author lawrence.daniels@gmail.com
  */
object Reply {

  implicit val ReplyFormat = Json.format[Reply]

  implicit val ReplyHandler = Macros.handler[Reply]

}