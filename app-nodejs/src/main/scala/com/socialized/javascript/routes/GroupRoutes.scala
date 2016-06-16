package com.socialized.javascript.routes

import org.scalajs.nodejs.express.{Application, Request, Response}
import org.scalajs.nodejs.mongodb._
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.data.GroupDAO
import com.socialized.javascript.data.GroupDAO._
import com.socialized.javascript.forms.MaxResultsForm
import com.socialized.javascript.models.Group

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Group Routes
  * @author lawrence.daniels@gmail.com
  */
object GroupRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext, mongo: MongoDB) = {
    implicit val groupDAO = dbFuture.flatMap(_.getGroupDAO)

    // Group CRUD
    app.get("/api/group", (request: Request, response: Response, next: NextFunction) => getGroupByEntity(request, response, next))
    app.get("/api/group/:groupID", (request: Request, response: Response, next: NextFunction) => getGroupByID(request, response, next))
    app.get("/api/groups", (request: Request, response: Response, next: NextFunction) => getGroups(request, response, next))
  }

  /**
    * Retrieve a group by ID
    */
  def getGroupByID(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, groupDAO: Future[GroupDAO]) = {
    val groupID = request.params("groupID")
    groupDAO.flatMap(_.findOneFuture[Group]("_id" $eq groupID.$oid)) onComplete {
      case Success(Some(group)) => response.send(group); next()
      case Success(None) => response.notFound(); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve a group by entity (ID or name)
    */
  def getGroupByEntity(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, groupDAO: Future[GroupDAO]) = {
    val form = request.queryAs[GroupForm]
    val query = (form.name.map("name" $eq _: js.Any) ?? form.id.map("_id" $eq _.$oid)).toOption
    groupDAO map { coll =>
      query.map(coll.findOneFuture[Group](_)) match {
        case Some(task) =>
          task onComplete {
            case Success(Some(group)) => response.send(group)
            case Success(None) => response.notFound()
            case Failure(e) => response.internalServerError(e)
          }
        case None =>
          response.badRequest("Expected 'name' or 'id' parameters")
      }
    } onComplete {
      case Success(_) => next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve groups
    */
  def getGroups(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, groupDAO: Future[GroupDAO]) = {
    val maxResults = request.request.queryAs[MaxResultsForm].getMaxResults()
    groupDAO.flatMap(_.find().limit(maxResults).toArrayFuture[Group]) onComplete {
      case Success(groups) => response.send(groups); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Group Form
    * @author lawrence.daniels@gmail.com
    */
  @js.native
  trait GroupForm extends js.Object {
    var id: js.UndefOr[String] = js.native
    var name: js.UndefOr[String] = js.native
  }

}
