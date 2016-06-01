package com.socialized.controllers

import javax.inject.Inject

import com.socialized.dao.{EventDAO, GroupDAO, OrganizationDAO, UserDAO}
import com.socialized.forms.EntitySearchResultForm._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

/**
  * Search Controller
  * @author lawrence.daniels@gmail.com
  */
class SearchController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {
  private val groupDAO = GroupDAO(reactiveMongoApi)
  private val eventDAO = EventDAO(reactiveMongoApi)
  private val organizationDAO = OrganizationDAO(reactiveMongoApi)
  private val userDAO = UserDAO(reactiveMongoApi)

  def search(searchTerm: String, maxResults: Int) = Action.async {
    val outcome = for {
      users <- userDAO.search(searchTerm).map(_.map(_.toForm))
      events <- eventDAO.search(searchTerm).map(_.map(_.toForm))
      groups <- groupDAO.search(searchTerm).map(_.map(_.toForm))
      organizations <- organizationDAO.search(searchTerm).map(_.map(_.toForm))
    } yield users ::: events ::: groups ::: organizations

    outcome map { results =>
      Ok(Json.toJson(results.sortBy(_.name)))
    } recover { case e =>
      Logger.error(s"Search failed for '$searchTerm' ($maxResults max results)", e)
      InternalServerError(e.getMessage)
    }
  }

}
