package com.socialized.controllers

import javax.inject.Inject

import com.socialized.forms.GroupForm._
import com.socialized.models.Group
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument => BS}

/**
  * Groups Controller
  * @author lawrence.daniels@gmail.com
  */
class GroupsController @Inject()(val reactiveMongoApi: ReactiveMongoApi, ws: WSClient) extends Controller with MongoController with ReactiveMongoComponents {
  private val mcGroups = db.collection[BSONCollection]("groups")

  def getGroupByID(id: String) = Action.async {
    mcGroups.find(selector = BS("_id" -> BSONObjectID(id))).one[Group] map {
      case Some(group) => Ok(Json.toJson(group.toForm))
      case None => NotFound(id)
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

  def getGroupByName(name: String) = Action.async {
    mcGroups.find(selector = BS("name" -> name)).one[Group] map {
      case Some(group) => Ok(Json.toJson(group.toForm))
      case None => NotFound(name)
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

  def getGroups(maxResults: Int) = Action.async {
    mcGroups.find(selector = BS()).cursor[Group]().collect[List](maxDocs = maxResults) map { groups =>
      Ok(Json.toJson(groups.map(_.toForm)))
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

}
