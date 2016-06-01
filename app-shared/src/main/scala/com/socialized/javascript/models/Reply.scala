package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a reply
  * @author lawrence.daniels@gmail.com
  * @see [[ReplyLikes]]
  */
@ScalaJSDefined
class Reply extends js.Object {
  var _id: js.UndefOr[String] = _
  var text: js.UndefOr[String] = _
  var submitter: js.UndefOr[Submitter] = _
  var creationTime: js.UndefOr[js.Date] = _
  var lastUpdateTime: js.UndefOr[js.Date] = _

  // UI-only indicators
  var likeLoading: js.UndefOr[Boolean] = _
}

/**
  * Reply Companion
  * @author lawrence.daniels@gmail.com
  */
object Reply {

  def apply(text: String, submitter: Submitter) = {
    val reply = new Reply()
    reply.text = text
    reply.submitter = submitter
    reply.creationTime = new js.Date()
    reply
  }

}