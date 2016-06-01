package com.socialized.controllers

import javax.inject.Inject

import com.socialized.actors.WebSockets
import com.socialized.dao.UserDAO
import com.socialized.forms.UserForm._
import com.socialized.forms.{ProfileEditForm, UserForm}
import com.socialized.models.{OperationResult, Submitter}
import com.socialized.util.OptionHelper._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller, Result}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONDocument => BS, _}

import scala.concurrent.Future

/**
  * User Controller
  * @author lawrence.daniels@gmail.com
  */
class UserController @Inject()(val reactiveMongoApi: ReactiveMongoApi, ws: WSClient) extends Controller
  with MongoController with ReactiveMongoComponents {
  private val userDAO = UserDAO(reactiveMongoApi)

  /////////////////////////////////////////////////////////////////////////////////
  //      User CRUD
  /////////////////////////////////////////////////////////////////////////////////

  def createUser = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[UserForm]) match {
      case Some(form) =>
        userDAO.create(form.toModel) map { user =>
          Ok(Json.toJson(user.toForm))
        } recover { case e =>
          InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("User object expected"))
    }
  }

  def getUserByID(id: String) = Action.async {
    userDAO.findOne(id) map {
      case Some(user) => Ok(Json.toJson(user.toForm))
      case None => NotFound(id)
    } recover { case e =>
      e.printStackTrace()
      InternalServerError(e.getMessage)
    }
  }

  def getUserByName(username: String) = Action.async {
    userDAO.findByUsername(username) map {
      case Some(user) => Ok(Json.toJson(user.toForm))
      case None => NotFound(username)
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

  def getUserByEmail(primaryEmail: String) = Action.async {
    userDAO.findByEmail(primaryEmail) map {
      case Some(user) => Ok(Json.toJson(user.toForm))
      case None => NotFound(primaryEmail)
    } recover { case e =>
      InternalServerError(e.getMessage)
    }
  }

  def updateUser = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[ProfileEditForm]) match {
      case Some(form) =>
        userDAO.update(form) map {
          case Some(user) => Ok(Json.toJson(user.toForm))
          case None => NotFound(form._id.map(_.stringify).getOrElse(""))
        } recover { case e =>
          InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(NotFound("User not found"))
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Avatars
  /////////////////////////////////////////////////////////////////////////////////

  def getAvatarByID(id: String) = Action.async { request =>
    val host = request.headers("host")
    for {
      user <- userDAO.findOne(id)
      avatarId = user.flatMap(_.avatarId)
      avatarURL = user.flatMap(_.avatarURL)
      response <- avatarURL match {
        case Some(url) =>
          val fullUrl = if (url.startsWith("/")) s"http://$host" + url else url
          getImageBinary(fullUrl)
        case None => getImageBinary(s"http://$host/assets/images/avatars/anonymous.png")
      }
    } yield response
  }

  /**
    * Retrieve a submitter by ID
    * @example GET /api/user/5633c756d9d5baa77a714803/submitter
    */
  def getSubmitter(ownerID: String) = Action.async {
    userDAO.findOne(ownerID, BS("avatarURL" -> 1, "firstName" -> 1, "lastName" -> 1)) map {
      case Some(user) => Ok(Json.toJson(user map (u => Submitter(u._id.map(_.stringify), u.fullName, u.avatarURL))))
      case None => NotFound(ownerID)
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  private def getImageBinary(imageURL: String): Future[Result] = {
    ws.url(imageURL).getStream().map { case (response, body) =>
      // check that the response was successful
      if (response.status == 200) {
        // get the content type
        val contentType =
          response.headers.get("Content-Type").flatMap(_.headOption).getOrElse("application/octet-stream")

        // if there's a content length, send that, otherwise return the body chunked
        response.headers.get("Content-Length") match {
          case Some(Seq(length)) =>
            Ok.feed(body).as(contentType).withHeaders("Content-Length" -> length)
          case _ =>
            Ok.chunked(body).as(contentType)
        }
      } else BadGateway(imageURL)
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Endorsers (Like / UnLike)
  /////////////////////////////////////////////////////////////////////////////////

  def getEndorsers(id: String) = Action.async {
    userDAO.findEndorsers(id) map { users =>
      Ok(Json.toJson(users.map(_.toForm)))
    } recover { case e =>
      e.printStackTrace()
      InternalServerError(e.getMessage)
    }
  }

  def like(id: String, endorserID: String) = Action.async {
    userDAO.like(id, endorserID) map {
      case Some(user) =>
        WebSockets ! user
        Ok(Json.toJson(OperationResult(success = true)))
      case None =>
        Ok(Json.toJson(OperationResult(success = false, message = "User not found")))
    }
  }

  def unlike(id: String, endorserID: String) = Action.async {
    userDAO.unlike(id, endorserID) map {
      case Some(user) =>
        WebSockets ! user
        Ok(Json.toJson(OperationResult(success = true)))
      case None =>
        Ok(Json.toJson(OperationResult(success = false, message = "User not found")))
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Followers (Follow / UnFollow)
  /////////////////////////////////////////////////////////////////////////////////

  def getFollowers(id: String) = Action.async {
    userDAO.findFollowers(id) map { users =>
      Ok(Json.toJson(users.map(_.toForm)))
    } recover { case e =>
      e.printStackTrace()
      InternalServerError(e.getMessage)
    }
  }

  def follow(id: String, followerID: String) = Action.async {
    userDAO.follow(id, followerID) map {
      case Some(user) =>
        WebSockets ! user
        Ok(Json.toJson(OperationResult(success = true)))
      case None =>
        Ok(Json.toJson(OperationResult(success = false, message = "User not found")))
    }
  }

  def unfollow(id: String, followerID: String) = Action.async {
    userDAO.unfollow(id, followerID) map {
      case Some(user) =>
        WebSockets ! user
        Ok(Json.toJson(OperationResult(success = true)))
      case None =>
        Ok(Json.toJson(OperationResult(success = false, message = "User not found")))
    }
  }

}
