package com.socialized.controllers

import javax.inject.Inject

import com.socialized.dao.SessionDAO
import com.socialized.forms.UserSessionForm._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

/**
  * Session Management Controller
  * @author lawrence.daniels@gmail.com
  */
class SessionController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents {
  private val sessionDAO = SessionDAO(reactiveMongoApi)

  def getSessionsByUsers(userIDs: List[String]) = Action.async {
    sessionDAO.findByUsers(userIDs) map { sessions =>
      Ok(Json.toJson(sessions.map(_.toForm)))
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

  /**
    * Retrieves a user session by ID
    * @param id the given user session ID
    */
  def getSession(id: String) = Action.async {
    sessionDAO.findOne(id) map {
      case Some(session) => Ok(Json.toJson(session.toForm))
      case None => NotFound(id)
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

}
