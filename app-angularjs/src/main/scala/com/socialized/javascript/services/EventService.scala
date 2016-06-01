package com.socialized.javascript.services

import com.github.ldaniels528.meansjs.angularjs.Service
import com.github.ldaniels528.meansjs.angularjs.http.Http
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import com.socialized.javascript.models.Event

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Events Service (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class EventService($http: Http) extends Service {

  /**
    * Creates a new event
    * @param userID the given [[String user ID]]
    * @param event  the given [[Event event]]
    * @return a promise of the newly created event
    */
  def createEvent(userID: String, event: Event)(implicit ec: ExecutionContext) = {
    $http.post[Event](s"/api/events/user/$userID", event).map(_.data)
  }

  /**
    * Retrieves all events for a user by user ID
    * @param userID the given [[String user ID]]
    * @return a promise of a [[com.socialized.javascript.models.User user]]
    */
  def getEvents(userID: String)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Event]](s"/api/events/user/$userID").map(_.data)
  }

  /**
    * Retrieves upcoming events for a user by user ID
    * @param userID the given [[String user ID]]
    * @return a promise of a [[com.socialized.javascript.models.User user]]
    */
  def getUpcomingEvents(userID: String)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Event]](s"/api/events/upcoming/$userID").map(_.data)
  }

}
