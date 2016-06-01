package com.socialized.models

import java.util.Date

import reactivemongo.bson.{BSONObjectID, Macros}
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}

/**
  * Represents a Post model object
  * @author lawrence.daniels@gmail.com
  */
case class Post(_id: Option[BSONObjectID],
                submitterId: Option[String],
                text: Option[String],
                summary: Option[SharedContent],
                likes: Option[Int],
                likedBy: Option[List[String]],
                creationTime: Option[Date],
                lastUpdateTime: Option[Date],
                attachments: Option[List[String]],
                comments: Option[List[Comment]],
                replyLikes: Option[List[ReplyLikes]],
                tags: Option[List[String]])

/**
  * Post Companion
  * @author lawrence.daniels@gmail.com
  */
object Post {

  implicit val PostHandler = Macros.handler[Post]

}
