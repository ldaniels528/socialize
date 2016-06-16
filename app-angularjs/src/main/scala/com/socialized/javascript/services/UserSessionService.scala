package com.socialized.javascript.services

import org.scalajs.angularjs.Service
import org.scalajs.angularjs.http.Http
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.models.Session

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * User Session Service
  * @author lawrence.daniels@gmail.com
  */
class UserSessionService($http: Http) extends Service {

  /**
    * Retrieves a user session by ID or creates a new anonymous session
    * @param id the given user session ID
    * @return a promise of a [[com.socialized.javascript.models.Session user session]]
    */
  def getSession(id: String)(implicit ec: ExecutionContext) = {
    $http.get[Session](s"/api/session/$id")
  }

  /**
    * Attempts to retrieve all of the sessions for the given collection of user IDs
    * @param userIDs the given collection of user IDs
    * @return a promise of an array of user sessions
    */
  def getSessions(userIDs: js.Array[String])(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Session]](s"/api/sessions?${userIDs.map(id => s"userIDs=$id").mkString("&")}")
  }

}
