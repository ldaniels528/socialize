package com.socialized.javascript.data

import com.socialized.javascript.models._
import org.scalajs.nodejs.mongodb.{MongoDB, ObjectID}
import org.scalajs.sjs.JsUnderOrHelper._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a Post data object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class PostData(var _id: js.UndefOr[ObjectID] = js.undefined,
               var text: js.UndefOr[String] = js.undefined,
               var submitterId: js.UndefOr[String] = js.undefined,
               var summary: js.UndefOr[SharedContent] = js.undefined,
               var likes: js.UndefOr[Int] = js.undefined,
               var likedBy: js.UndefOr[js.Array[String]] = js.undefined,
               var creationTime: js.Date = new js.Date(),
               var lastUpdateTime: js.Date = new js.Date(),

               // collections
               var attachments: js.UndefOr[js.Array[String]] = js.undefined,
               var comments: js.UndefOr[js.Array[Comment]] = js.undefined,
               var replyLikes: js.UndefOr[js.Array[ReplyLikes]] = js.undefined,
               var tags: js.UndefOr[js.Array[String]] = js.undefined) extends js.Object

/**
  * Post Data Companion
  * @author lawrence.daniels@gmail.com
  */
object PostData {

  /**
    * Post Data Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class PostDataExtensions(val data: PostData) extends AnyVal {

    def toModel = new Post(
      _id = data._id.map(_.toHexString()),
      text = data.text,
      submitterId = data.submitterId,
      summary = data.summary,
      likes = data.likes,
      likedBy = data.likedBy,
      creationTime = data.creationTime,
      lastUpdateTime = data.lastUpdateTime,

      // collections
      attachments = data.attachments,
      comments = data.comments,
      replyLikes = data.replyLikes,
      tags = data.tags
    )
  }

  /**
    * Post Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class PostExtensions(val post: Post) extends AnyVal {

    def toData(implicit mongo: MongoDB) = new PostData(
      _id = post._id.map(mongo.ObjectID(_)) ?? mongo.ObjectID(),
      text = post.text,
      submitterId = post.submitterId,
      summary = post.summary,
      likes = post.likes,
      likedBy = post.likedBy,
      creationTime = post.creationTime,
      lastUpdateTime = post.lastUpdateTime,

      // collections
      attachments = post.attachments,
      comments = post.comments,
      replyLikes = post.replyLikes,
      tags = post.tags
    )

  }

}