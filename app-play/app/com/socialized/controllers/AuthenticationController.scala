package com.socialized.controllers

import javax.inject.Inject

import com.socialized.dao.{CredentialDAO, SessionDAO, UserDAO}
import com.socialized.forms.UserSessionForm._
import com.socialized.forms.{CredentialForm, LoginForm}
import com.socialized.models.UserSession
import com.socialized.models.UserSession._
import com.socialized.util.OptionHelper._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.json.Json.{obj => JS, _}
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.Future

/**
  * Authentication Controller
  * @author lawrence.daniels@gmail.com
  */
class AuthenticationController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with CredentialsManagement {

  private val credentialDAO = CredentialDAO(reactiveMongoApi)
  private val sessionDAO = SessionDAO(reactiveMongoApi)
  private val userDAO = UserDAO(reactiveMongoApi)

  /**
    * Authenticates a user via JSON body containing a [[LoginForm login form]]
    * @example POST /api/signin => { "screenName":"ldaniels", "password":"@sadwqwe@%#" }
    */
  def authenticate = Action.async { implicit request =>
    val result = for {
      form <- request.body.asJson.flatMap(_.asOpt[LoginForm])
      screenName <- form.screenName
      password <- form.password
    } yield (screenName, password)

    result match {
      case Some((screenName, password)) =>
        val outcome = for {
          passed <- authenticateUser(screenName, password)
          _ = if (!passed) throw new IllegalStateException("Either the username or password was invalid")
          user_? <- userDAO.findByUsername(screenName)
          session <- user_? match {
            case Some(user) => sessionDAO.create(UserSession.toSession(user))
            case None => throw new IllegalStateException("Either the username or password was invalid")
          }
        } yield session

        outcome map { session =>
          Ok(Json.toJson(session.toForm))
        } recover {
          case e: IllegalStateException =>
            BadRequest(e.getMessage)
          case e =>
            e.printStackTrace()
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Username and password are required"))
    }
  }

  /**
    * Updates an existing set of credentials
    * @example GET http://localhost:9000/api/jobs
    */
  def updateCredentials = Action.async { implicit request =>
    val result = for {
      js <- request.body.asJson
      credential <- js.asOpt[CredentialForm]
      password <- (js \ "password").asOpt[String]
    } yield (credential, password)

    result match {
      case Some((credential, password)) =>
        credentialDAO.update(performHash(credential.toModel, password)) map { updatedCredential =>
          Ok(JS("success" -> updatedCredential.nonEmpty))
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Credential object expected"))
    }
  }

  /**
    * Performs the actual authentication task
    * @param screenName the given user/screen name
    * @param password   the given password
    * @return a promise of an authenticated (true) or non-authenticated (false) result
    */
  private def authenticateUser(screenName: String, password: String): Future[Boolean] = {
    credentialDAO.findByUsername(screenName) map {
      case Some(credential) =>
        credential.hashPassword.exists(pwd => encryptionHelper.compare(password, pwd))
      case _ =>
        Logger.warn(s"Screen name '$screenName' not found")
        false
    }
  }

}

