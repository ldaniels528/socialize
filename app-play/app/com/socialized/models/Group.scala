package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson._

/**
  * Represents a Group model object
  * @author lawrence.daniels@gmail.com
  */
case class Group(_id: Option[BSONObjectID] = BSONObjectID.generate,
                 name: Option[String],
                 avatarId: Option[String],
                 avatarURL: Option[String],
                 creationTime: Option[Date] = new Date())

/**
  * Group Companion
  * @author lawrence.daniels@gmail.com
  */
object Group {

  implicit val GroupFormat = Json.format[Group]

  implicit val GroupHandler = Macros.handler[Group]

}