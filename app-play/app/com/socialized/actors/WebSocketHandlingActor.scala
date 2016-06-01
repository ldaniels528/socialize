package com.socialized.actors

import java.util.UUID

import akka.actor._
import play.api.libs.json.JsValue

/**
 * Web Socket Handling Actor
 * @author lawrence.daniels@gmail.com
 */
class WebSocketHandlingActor(out: ActorRef) extends Actor with ActorLogging {
  private val uuid = UUID.randomUUID()

  override def preStart() = WebSockets.register(uuid, self)

  override def postStop() = WebSockets.unregister(uuid)

  override def receive = {
    case message: JsValue =>
      //log.info(s"Sending message $message")
      out ! message

    case message =>
      log.warning(s"Unhandled message $message")
      unhandled(message)
  }
}

/**
 * Web Socket Handling Actor Companion
 * @author lawrence.daniels@gmail.com
 */
object WebSocketHandlingActor {

  def props(out: ActorRef) = Props(new WebSocketHandlingActor(out))

}