package com.socialized.controllers

import javax.inject.Inject

import com.socialized.dao.{DAOException, EventDAO}
import com.socialized.forms.EventForm
import com.socialized.forms.EventForm._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.Future

/**
  * Events Controller
  * @author lawrence.daniels@gmail.com
  */
class EventController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {
  private val eventDAO = EventDAO(reactiveMongoApi)

  def createEvent(sessionID: String) = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[EventForm]) match {
      case Some(form) =>
        eventDAO.create(form.toModel) map { newEvent =>
          Ok(Json.toJson(newEvent.toForm))
        } recover {
          case e: DAOException => BadRequest(e.getMessage)
          case e =>
            Logger.error(s"Failed to create event: ${Json.toJson(form)}", e)
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Event expected"))
    }
  }

  def getEventsByUserID(userID: String) = Action.async {
    eventDAO.findEventsByOwner(userID) map { events =>
      Ok(Json.toJson(events.map(_.toForm)))
    } recover {
      case e: DAOException => BadRequest(e.getMessage)
      case e =>
        Logger.error(s"Failed to retrieve events for user '$userID'", e)
        InternalServerError(e.getMessage)
    }
  }

}
