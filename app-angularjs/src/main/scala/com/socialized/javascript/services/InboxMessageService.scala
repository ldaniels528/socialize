package com.socialized.javascript.services

import io.scalajs.npm.angularjs.Service
import io.scalajs.npm.angularjs.http.Http
import io.scalajs.util.ScalaJsHelper._
import com.socialized.javascript.models.InboxMessage

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Inbox Message Service
  * @author lawrence.daniels@gmail.com
  */
class InboxMessageService($http: Http) extends Service {

  /**
    * Retrieves messages for the given user
    * @param userID the ID of the user that owns the messages
    * @return a promise of an array of [[InboxMessage messages]]
    */
  def getMessages(userID: String)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[InboxMessage]](s"/api/messages/user/$userID")
  }

}

