package com.socialized.controllers

import javax.inject.Inject

import com.socialized.forms.InboxMessageForm
import com.socialized.forms.InboxMessageForm._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Future

/**
  * Inbox Messages Controller
  * @author lawrence.daniels@gmail.com
  */
class InboxMessagesController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {
  private val mcInbox = db.collection[BSONCollection]("inbox")

  def getMessages(userID: String) = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[InboxMessageForm]) match {
      case Some(form) =>
        val message = form.toModel
        mcInbox.insert(message) map {
          case result if result.hasErrors => InternalServerError(result.message)
          case result => Ok(Json.toJson(message.toForm))
        } recover { case e =>
          InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Inbox message expected"))
    }
  }

}
