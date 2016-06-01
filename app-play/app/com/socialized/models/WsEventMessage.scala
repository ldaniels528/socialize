package com.socialized.models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

/**
  * WebSocket Event Message
  * @author lawrence.daniels@gmail.com
  */
case class WsEventMessage(action: String, data: String)

/**
  * WebSocket Event Message Companion
  * @author lawrence.daniels@gmail.com
  */
object WsEventMessage {
  val NOTIFICATION = "NOTIFICATION"
  val POST = "POST"
  val USER = "USER"

  implicit val WsEventFormat = Json.format[WsEventMessage]

  implicit val WsEventHandler = Macros.handler[WsEventMessage]

}