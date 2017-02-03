package com.socialized.javascript.services

import io.scalajs.npm.angularjs.http.Http
import io.scalajs.npm.angularjs.{Service, injected}
import io.scalajs.util.ScalaJsHelper._
import com.socialized.javascript.models.{Notification, OperationResult, WsEventMessage}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Notifications Service (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class NotificationService($http: Http, @injected("WebSocketService") webSocketSvc: WebSocketService) extends Service {

  /**
    * Creates a new notification
    * @param userID       the given [[String user ID]]
    * @param notification the given [[Notification notification]]
    * @return a promise of the newly created notification
    */
  def createNotification(userID: String, notification: Notification)(implicit ec: ExecutionContext) = {
    $http.post[Notification](s"/api/notifications/user/$userID", notification)
  }

  /**
    * Deletes an existing notification by ID
    * @param notificationID the given [[String user ID]]
    * @return a promise of an [[OperationResult indicator]] as to whether the notification was deleted
    */
  def deleteNotification(notificationID: String)(implicit ec: ExecutionContext) = {
    $http.delete[OperationResult](s"/api/notifications/$notificationID")
  }

  /**
    * Retrieves notifications by user ID
    * @param maxResults the maximum number of results to return
    * @return a promise of an array of [[Notification notifications]]
    */
  def getNotifications(maxResults: Int = 10)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Notification]](s"/api/notifications?maxResults=$maxResults")
  }

  /**
    * Retrieves notifications by user ID
    * @param userID the given [[String user ID]]
    * @param unread indicates whether returned notifications should be unread or mixed
    * @return a promise of an array of [[Notification notifications]]
    */
  def getNotificationsByUserID(userID: String, unread: Boolean)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Notification]](s"/api/notifications/user/$userID/$unread")
  }

  /**
    * Publishes a new notification:
    * <ol>
    * <li>Sends the event via web socket</li>
    * <li>Persists the event in the database</li>
    * </ol>
    * @param userID       the given [[String user ID]]
    * @param notification the given [[Notification notification]]
    * @return a promise of the newly created notification
    */
  def publishNotification(userID: String, notification: Notification)(implicit ec: ExecutionContext) = {
    webSocketSvc.send(WsEventMessage(WsEventMessage.NOTIFICATION, notification))
    createNotification(userID, notification)
  }

}
