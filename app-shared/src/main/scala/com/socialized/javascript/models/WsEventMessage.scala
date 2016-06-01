package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * WebSocket Event Message
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class WsEventMessage extends js.Object {
  var action: js.UndefOr[String] = _
  var data: js.UndefOr[String] = _
}

/**
  * WebSocket Event Message Companion
  * @author lawrence.daniels@gmail.com
  */
object WsEventMessage {
  val NOTIFICATION = "NOTIFICATION"
  val POST = "POST"
  val TEST = "TEST"

  def apply(action: String, data: js.Object) = {
    val message = new WsEventMessage()
    message.action = action
    message.data = JSON.stringify(data)
    message
  }

}