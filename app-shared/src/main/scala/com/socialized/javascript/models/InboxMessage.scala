package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents an inbox message
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class InboxMessage extends js.Object {
  var _id: js.UndefOr[String] = _
  var title: js.UndefOr[String] = _
  var message: js.UndefOr[String] = _
  var creationTime: js.UndefOr[js.Date] = _
}

/**
  * Inbox Message Companion
  * @author lawrence.daniels@gmail.com
  */
object InboxMessage {

  def apply(title: String, message: String, creationTime: js.Date) = {
    val msg = new InboxMessage()
    msg.title = title
    msg.message = message
    msg.creationTime = creationTime
    msg
  }
}

