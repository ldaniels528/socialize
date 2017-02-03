package com.socialized.javascript.routes

import io.scalajs.npm.express.{Application, Request, Response}
import io.scalajs.npm.mongodb._
import com.socialized.javascript.data.EventDAO
import com.socialized.javascript.models.Event
import com.socialized.javascript.data.EventDAO._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Event Routes
  * @author lawrence.daniels@gmail.com
  */
object EventRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext) = {
    implicit val eventDAO = dbFuture.flatMap(_.getEventDAO)

    app.get("/api/events/user/:ownerID", (request: Request, response: Response, next: NextFunction) => getEventsByOwner(request, response, next))
    app.get("/api/events/upcoming/:ownerID", (request: Request, response: Response, next: NextFunction) => getUpcomingEvents(request, response, next))
  }

  /**
    * Retrieve events by user/owner
    */
  def getEventsByOwner(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, eventDAO: Future[EventDAO]) = {
    val ownerID = request.params("ownerID")
    eventDAO.flatMap(_.find("owner._id" $eq ownerID.$oid).toArrayFuture[Event]) onComplete {
      case Success(events) => response.send(events); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve upcoming events by user/owner
    */
  def getUpcomingEvents(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, eventDAO: Future[EventDAO]) = {
    val ownerID = request.params("ownerID")
    eventDAO.flatMap(_.find("owner._id" $eq ownerID.$oid).toArrayFuture[Event]) onComplete {
      case Success(events) => response.send(events); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

}
