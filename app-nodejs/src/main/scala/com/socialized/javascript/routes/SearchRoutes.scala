package com.socialized.javascript.routes

import org.scalajs.nodejs.express.{Application, Request, Response}
import org.scalajs.nodejs.mongodb._
import com.socialized.javascript.data.EventDAO._
import com.socialized.javascript.data.GroupDAO._
import com.socialized.javascript.data.UserDAO._
import com.socialized.javascript.data.{EventDAO, GroupDAO, UserDAO}
import com.socialized.javascript.forms.MaxResultsForm
import com.socialized.javascript.models._

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Search Routes
  * @author lawrence.daniels@gmail.com
  */
object SearchRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext, mongo: MongoDB) = {
    implicit val events = dbFuture.flatMap(_.getEventDAO).map(EventSearchAgent(_))
    implicit val groups = dbFuture.flatMap(_.getGroupDAO).map(GroupSearchAgent(_))
    implicit val users = dbFuture.flatMap(_.getUserDAO).map(UserSearchAgent(_))

    /**
      * Searches for people, groups, organizations and events
      * @example GET /api/search?searchTerm=mic&maxResults=10
      */
    app.get("/api/search", (request: Request, response: Response, next: NextFunction) => getSearchResults(request, response, next))

  }

  def getSearchResults(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, events: Future[EventSearchAgent], groups: Future[GroupSearchAgent], users: Future[UserSearchAgent]) = {
    val searchAgents = Seq(events, groups, users)
    val form = request.queryAs[SearchForm]

    def search(searchTerm: String, maxResults: Int) = Future.sequence(searchAgents map (_.flatMap(_.search(searchTerm, maxResults)))) map (_.flatten)

    form.searchTerm.toOption map ((_, form.getMaxResults())) match {
      case Some((searchTerm, maxResults)) =>
        search(searchTerm, maxResults) onComplete {
          case Success(searchResults) => response.send(js.Array(searchResults: _*)); next()
          case Failure(e) => response.internalServerError(e); next()
        }
      case None =>
        response.badRequest("Bad Request: searchTerm is required"); next()
    }
  }

  /**
    * Search Form
    * @author lawrence.daniels@gmail.com
    */
  @js.native
  trait SearchForm extends MaxResultsForm {
    var searchTerm: js.UndefOr[String] = js.native
  }

  /**
    * Abstract Search Agent
    * @author lawrence.daniels@gmail.com
    */
  trait SearchAgent[T <: js.Any] {

    def coll: Collection

    def fields: js.Array[String]

    def search(searchTerm: String, maxResults: Int)(implicit ec: ExecutionContext) = {
      coll.find(selector = getSelection(searchTerm)).limit(maxResults).toArrayFuture[T] map (_ map toSearchResult)
    }

    def toSearchResult(entity: T): EntitySearchResult

    private def getSelection(searchTerm: String) = {
      fields.foldLeft[List[(String, js.Any)]](Nil) { (list, field) =>
        (field $regex(s"^$searchTerm", ignoreCase = true)) :: list
      } match {
        case Nil => doc()
        case one :: Nil => doc(one)
        case many => doc($or(many: _*))
      }
    }

  }

  /**
    * Event Search Agent
    * @author lawrence.daniels@gmail.com
    */
  case class EventSearchAgent(coll: EventDAO) extends SearchAgent[Event] {
    val fields = js.Array("title")

    override def toSearchResult(event: Event) = {
      new EntitySearchResult(
        _id = event._id,
        name = event.title,
        description = "Event",
        `type` = "EVENT",
        avatarURL = event.avatarURL,
        creationTime = event.creationTime
      )
    }
  }

  /**
    * Group Search Agent
    * @author lawrence.daniels@gmail.com
    */
  case class GroupSearchAgent(coll: GroupDAO) extends SearchAgent[Group] {
    val fields = js.Array("name")

    override def toSearchResult(group: Group) = {
      new EntitySearchResult(
        _id = group._id,
        name = group.name,
        description = "Group",
        `type` = "GROUP",
        avatarURL = group.avatarURL,
        creationTime = group.creationTime
      )
    }
  }

  /**
    * User Search Agent
    * @author lawrence.daniels@gmail.com
    */
  case class UserSearchAgent(coll: UserDAO) extends SearchAgent[User] {
    val fields = js.Array("firstName", "lastName", "primaryEmail")

    override def toSearchResult(user: User) = {
      new EntitySearchResult(
        _id = user._id,
        name = user.fullName,
        description = "User",
        `type` = "USER",
        avatarURL = user.avatarURL,
        creationTime = user.creationTime
      )
    }
  }

}
