package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{Macros, _}

/**
  * Represents a Entity Search Result (e.g. Users, Groups, etc.)
  * @author lawrence.daniels@gmail.com
  */
case class EntitySearchResult(_id: Option[BSONObjectID] = BSONObjectID.generate,
                              name: Option[String],
                              description: Option[String],
                              `type`: Option[String],
                              avatarURL: Option[String],
                              creationTime: Option[Date] = new Date())

/**
  * Entity Search Result Companion Object
  * @author lawrence.daniels@gmail.com
  */
object EntitySearchResult {

  implicit val EntitySearchResultFormat = Json.format[EntitySearchResult]

  implicit val EntitySearchResultHandler = Macros.handler[EntitySearchResult]

}