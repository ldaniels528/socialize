package com.socialized.javascript.routes

import org.scalajs.nodejs.express.{Application, Request, Response}
import org.scalajs.nodejs.mongodb._
import com.socialized.javascript.data.NotificationDAO
import com.socialized.javascript.data.NotificationDAO._
import com.socialized.javascript.forms.MaxResultsForm
import com.socialized.javascript.models.Notification

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Notification Routes
  * @author lawrence.daniels@gmail.com
  */
object NotificationRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext, mongo: MongoDB) = {
    implicit val notificationDAO = dbFuture.flatMap(_.getNotificationDAO)

    app.get("/api/notifications", (request: Request, response: Response, next: NextFunction) => getNotifications(request, response, next))
    app.get("/api/notifications/user/:ownerID/:unread", (request: Request, response: Response, next: NextFunction) => getNotificationsByOwner(request, response, next))
  }

  /**
    * Retrieve notifications
    * @example GET /api/notifications?maxResults=20
    */
  def getNotifications(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, notificationDAO: Future[NotificationDAO]) = {
    val maxResults = request.request.queryAs[MaxResultsForm].getMaxResults()
    notificationDAO.flatMap(_.find().limit(maxResults).toArrayFuture[Notification]) onComplete {
      case Success(notifications) => response.send(notifications); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve notifications by owner/user
    * @example GET /api/notifications/user/5633c756d9d5baa77a714803/true
    */
  def getNotificationsByOwner(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, notificationDAO: Future[NotificationDAO]) = {
    val ownerID = request.params("ownerID")
    val unread = java.lang.Boolean.valueOf(request.params("unread")).booleanValue()
    notificationDAO.flatMap(_.find("owner._id" $eq ownerID.$oid).toArrayFuture[Notification]) onComplete {
      case Success(notifications) => response.send(notifications); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

}
