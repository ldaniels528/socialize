package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson._
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}

/**
  * Represents a comment
  * @author lawrence.daniels@gmail.com
  */
case class Comment(_id: Option[String] = BSONObjectID.generate.stringify,
                   text: Option[String],
                   submitter: Option[Submitter],
                   likes: Option[Int],
                   likedBy: Option[List[String]],
                   replies: Option[List[Reply]],
                   creationTime: Option[Date],
                   lastUpdateTime: Option[Date])

/**
  * Comment Companion
  * @author lawrence.daniels@gmail.com
  */
object Comment {

  implicit val CommentFormat = Json.format[Comment]

  implicit val CommentHandler = Macros.handler[Comment]

}
