package com.socialized.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.RoundRobinPool
import com.socialized.models.WsEventMessage
import play.api.Logger
import play.api.libs.json.Json
import play.libs.Akka
import reactivemongo.api.BSONSerializationPack
import reactivemongo.bson.BSON

import scala.collection.concurrent.TrieMap

/**
  * Web Sockets Singleton
  * @author lawrence.daniels@gmail.com
  */
object WebSockets {
  private val actors = TrieMap[UUID, ActorRef]()
  private val system = Akka.system
  private val relayActor = system.actorOf(Props[WsRelayActor].withRouter(RoundRobinPool(nrOfInstances = 50)), name = "WsRelay")

  /**
    * Broadcasts the given message to all connected users
    * @param message the given message
    */
  def !(message: WsEventMessage): Unit = relayActor ! message

  /**
    * Broadcasts an event message to all connected users
    * @param entity the given entity to be converted into a [[WsEventMessage event message]]
    */
  def ![A](entity: A)(implicit writer: BSONSerializationPack.Writer[A]): Unit = {
    relayActor ! WsEventMessage(entity.getClass.getSimpleName.toUpperCase(), BSON.write(entity).toString())
  }

  /**
    * Registers the given actor reference
    * @param uuid the given [[java.util.UUID unique identifier]]
    * @param actor the given [[akka.actor.ActorRef actor reference]]
    */
  def register(uuid: UUID, actor: ActorRef) = {
    Logger.info(s"Registering web socket actor for session # $uuid...")
    actors(uuid) = actor
  }

  /**
    * Un-registers the given actor reference
    * @param uuid the given [[java.util.UUID unique identifier]]
    * @return the option of the [[akka.actor.ActorRef actor reference]] being removed
    */
  def unregister(uuid: UUID): Option[ActorRef] = actors.remove(uuid)

  /**
    * Web Socket Relay Actor
    * @author lawrence.daniels@gmail.com
    */
  class WsRelayActor() extends Actor with ActorLogging {
    override def receive = {
      case message: WsEventMessage =>
        actors.foreach { case (uid, actor) =>
          actor ! Json.toJson(message)
        }

      case message =>
        log.warning(s"Unhandled message $message")
        unhandled(message)
    }
  }

}

