package com.socialized.models

/**
  * Represents the Like action for a Reply
  * <b>NOTE</b>: due to a MongoDB restriction for deeply nested objects,
  * likes could not be included in the [[Reply reply class]]
  * @author lawrence.daniels@gmail.com
  */
case class ReplyLikes(_id: Option[String], // NOTE: this is the replyID!
                      likes: Option[Int],
                      likedBy: Option[List[String]])

/**
  * Reply Likes Companion
  * @author lawrence.daniels@gmail.com
  */
object ReplyLikes {

  implicit val ReplyLikesFormat = play.api.libs.json.Json.format[ReplyLikes]

  implicit val ReplyLikesHandler = reactivemongo.bson.Macros.handler[ReplyLikes]

}