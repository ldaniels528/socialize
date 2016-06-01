package com.socialized.controllers

import java.util.Date
import javax.inject.Inject

import com.socialized.forms.NotificationForm._
import com.socialized.models.{Notification, OperationResult}
import com.socialized.util.OptionHelper._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument => BS}

import scala.concurrent.Future

/**
  * Notification Controller
  * @author lawrence.daniels@gmail.com
  */
class NotificationController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {

  private val mcNotifications = db.collection[BSONCollection]("notifications")

  def createNotification(userID: String) = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[Notification]) match {
      case Some(notification) =>
        val newNotification = notification.copy(_id = BSONObjectID.generate, creationTime = new Date())
        mcNotifications.insert(newNotification) map {
          case result if result.inError =>
            InternalServerError("Notification could not be retrieved")
          case result =>
            Ok(Json.toJson(newNotification.toForm))
        } recover { case e =>
          Logger.error(s"Failed to create notification for user '$userID'", e)
          InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Notification expected"))
    }
  }

  def deleteNotification(notificationID: String) = Action.async {
    mcNotifications.remove(query = BS("_id" -> BSONObjectID(notificationID))) map { result =>
      Ok(Json.toJson(OperationResult(success = !result.inError, message = result.message)))
    }
  }

  def getNotifications(maxResults: Int) = Action.async {
    mcNotifications.find(selector = BS()).cursor[Notification]().collect[List](maxDocs = maxResults) map { notifications =>
      Ok(Json.toJson(notifications.map(_.toForm)))
    } recover { case e =>
      Logger.error(s"Failed to retrieve notifications", e)
      InternalServerError(e.getMessage)
    }
  }

  def getNotificationsByUserID(userID: String, unread: Boolean) = Action.async {
    mcNotifications.find(selector = BS("owner._id" -> BSONObjectID(userID))).cursor[Notification]().collect[List]() map { notifications =>
      Ok(Json.toJson(notifications.map(_.toForm)))
    } recover { case e =>
      Logger.error(s"Failed to retrieve notifications for user '$userID'", e)
      InternalServerError(e.getMessage)
    }
  }

}
