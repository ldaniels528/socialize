package com.socialized.javascript.models

import io.scalajs.util.ScalaJsHelper._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a comment
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Comment extends js.Object {
  var _id: js.UndefOr[String] = _
  var text: js.UndefOr[String] = _
  var submitter: js.UndefOr[Submitter] = _
  var likes: js.UndefOr[Int] = _
  var likedBy: js.UndefOr[js.Array[String]] = _
  var replies: js.UndefOr[js.Array[Reply]] = _
  var creationTime: js.UndefOr[js.Date] = _
  var lastUpdateTime: js.UndefOr[js.Date] = _

  // UI-only indicators
  var likeLoading: js.UndefOr[Boolean] = _
  var newReply: js.UndefOr[Boolean] = _
}

/**
  * Comment Companion
  * @author lawrence.daniels@gmail.com
  */
object Comment {

  def apply(text: String, submitter: Submitter) = {
    val comment = new Comment()
    comment.text = text
    comment.submitter = submitter
    comment.creationTime = new js.Date()
    comment.likes = 0
    comment.likedBy = emptyArray[String]
    comment
  }

}