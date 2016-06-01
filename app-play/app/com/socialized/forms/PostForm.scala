package com.socialized.forms

import java.util.Date

import com.socialized.models._
import com.socialized.util.OptionHelper._
import reactivemongo.bson.BSONObjectID

/**
  * Represents a Post form
  * @author lawrence.daniels@gmail.com
  */
case class PostForm(_id: Option[String],
                    submitterId: Option[String],
                    text: Option[String],
                    submitter: Option[Submitter],
                    summary: Option[SharedContent],
                    likes: Option[Int],
                    likedBy: Option[List[String]],
                    creationTime: Option[Date],
                    lastUpdateTime: Option[Date],
                    attachments: Option[List[String]],
                    comments: Option[List[Comment]],
                    replyLikes: Option[List[ReplyLikes]],
                    tags: Option[List[String]]) {

  /**
    * Transforms the post form into a post model
    * @return the [[Post post model]]
    */
  def toModel = Post(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    submitterId = submitterId,
    text = text,
    summary = summary,
    likes = likes,
    likedBy = likedBy,
    creationTime = creationTime ?? new Date(),
    lastUpdateTime = lastUpdateTime ?? new Date(),
    attachments = attachments,
    comments = comments,
    replyLikes = replyLikes,
    tags = tags
  )

}

/**
  * Post Form Companion
  * @author lawrence.daniels@gmail.com
  */
object PostForm {

  /**
    * Post Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class PostFormExtensions(val model: Post) extends AnyVal {

    /**
      * Transforms the post model into a post form
      * @return the [[PostForm post form]]
      */
    def toForm(submitter: Option[Submitter] = None) = PostForm(
      _id = model._id.map(_.stringify) ?? BSONObjectID.generate.stringify,
      submitterId = model.submitterId,
      text = model.text,
      submitter = submitter,
      summary = model.summary,
      likes = model.likes,
      likedBy = model.likedBy,
      creationTime = model.creationTime ?? new Date(),
      lastUpdateTime = model.lastUpdateTime ?? new Date(),
      attachments = model.attachments,
      comments = model.comments,
      replyLikes = model.replyLikes,
      tags = model.tags
    )

  }

  implicit val PostFormFormat = play.api.libs.json.Json.format[PostForm]

}
