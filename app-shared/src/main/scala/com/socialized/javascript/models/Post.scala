package com.socialized.javascript.models

import org.scalajs.nodejs.util.ScalaJsHelper._
import org.scalajs.sjs.JsUnderOrHelper._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a Post model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Post(var _id: js.UndefOr[String] = js.undefined,
           var text: js.UndefOr[String] = js.undefined,
           var submitter: js.UndefOr[Submitter] = js.undefined,
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
           var tags: js.UndefOr[js.Array[String]] = js.undefined,

           // Angular-specific properties
           var loading: js.UndefOr[Boolean] = js.undefined,
           var deleteLoading: js.UndefOr[Boolean] = js.undefined,
           var likeLoading: js.UndefOr[Boolean] = js.undefined,
           var newComment: js.UndefOr[Boolean] = js.undefined,
           var refreshLoading: js.UndefOr[Boolean] = js.undefined,
           var summaryLoaded: js.UndefOr[Boolean] = js.undefined,
           var summaryLoadQueued: js.UndefOr[Boolean] = js.undefined) extends js.Object {

  def copy(_id: js.UndefOr[String] = js.undefined,
           text: js.UndefOr[String] = js.undefined,
           submitter: js.UndefOr[Submitter] = js.undefined,
           submitterId: js.UndefOr[String] = js.undefined,
           summary: js.UndefOr[SharedContent] = js.undefined,
           likes: js.UndefOr[Int] = js.undefined,
           likedBy: js.UndefOr[js.Array[String]] = js.undefined,
           creationTime: js.UndefOr[js.Date] = js.undefined,
           lastUpdateTime: js.UndefOr[js.Date] = js.undefined,

           // collections
           attachments: js.UndefOr[js.Array[String]] = js.undefined,
           comments: js.UndefOr[js.Array[Comment]] = js.undefined,
           replyLikes: js.UndefOr[js.Array[ReplyLikes]] = js.undefined,
           tags: js.UndefOr[js.Array[String]] = js.undefined,

           // Angular-specific properties
           loading: js.UndefOr[Boolean] = js.undefined,
           likeLoading: js.UndefOr[Boolean] = js.undefined,
           newComment: js.UndefOr[Boolean] = js.undefined,
           refreshLoading: js.UndefOr[Boolean] = js.undefined,
           summaryLoaded: js.UndefOr[Boolean] = js.undefined,
           summaryLoadQueued: js.UndefOr[Boolean] = js.undefined) = {
    new Post(
      _id = _id ?? this._id,
      text = text ?? this.text,
      submitter = submitter ?? this.submitter,
      submitterId = submitterId ?? this.submitterId,
      summary = summary ?? this.summary,
      likes = likes ?? this.likes,
      likedBy = likedBy ?? this.likedBy,
      creationTime = creationTime.getOrElse(new js.Date()),
      lastUpdateTime = lastUpdateTime.getOrElse(new js.Date()),

      // collections
      attachments = attachments ?? this.attachments,
      comments = comments ?? this.comments,
      replyLikes = replyLikes ?? this.replyLikes,
      tags = tags ?? this.tags,

      // Angular-specific properties
      loading = loading ?? this.loading,
      likeLoading = likeLoading ?? this.likeLoading,
      newComment = newComment ?? this.newComment,
      refreshLoading = refreshLoading ?? this.refreshLoading,
      summaryLoaded = summaryLoaded ?? this.summaryLoaded,
      summaryLoadQueued = summaryLoadQueued ?? this.summaryLoadQueued
    )
  }

}

/**
  * Post Companion
  * @author lawrence.daniels@gmail.com
  */
object Post {

  def apply(user: User): Post = {
    val post = new Post()
    post.creationTime = new js.Date()
    post.lastUpdateTime = new js.Date()
    post.submitter = Submitter(user)
    post.submitterId = user._id
    post.text = ""
    post.likes = 0
    post.attachments = emptyArray[String]
    post.comments = emptyArray[Comment]
    post.likedBy = emptyArray[String]
    post.replyLikes = emptyArray[ReplyLikes]
    post.tags = emptyArray[String]
    post
  }

}