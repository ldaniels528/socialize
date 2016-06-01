package com.socialized.controllers

import javax.inject.Inject

import com.socialized.controllers.ApplicationConstants.ANONYMOUS_IMAGE_URL
import com.socialized.dao.{CredentialDAO, DAOException, SessionDAO, UserDAO}
import com.socialized.forms.SignUpForm
import com.socialized.forms.UserSessionForm._
import com.socialized.models.{Credential, User, UserSession}
import com.socialized.util.OptionHelper._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.Future

/**
  * Sign-Up Controller
  * @author lawrence.daniels@gmail.com
  */
class SignUpController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with CredentialsManagement {

  private val credentialDAO = CredentialDAO(reactiveMongoApi)
  private val sessionDAO = SessionDAO(reactiveMongoApi)
  private val userDAO = UserDAO(reactiveMongoApi)

  def signUp = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[SignUpForm]) map (form => (form, form.validate)) match {
      case Some((form, messages)) if messages.nonEmpty =>
        Future.successful(BadRequest(messages.mkString(", and ")))
      case Some((form, _)) =>
        val result = for {
          _ <- credentialDAO.create(makeCredential(form))
          user <- userDAO.create(makeUser(form))
          session <- sessionDAO.create(UserSession.toSession(user))
        } yield session

        result map { session =>
          Ok(Json.toJson(session.toForm))
        } recover {
          case e: DAOException =>
            BadRequest(e.getMessage)
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Sign-up information expected"))
    }
  }

  private def makeCredential(form: SignUpForm) = {
    val encrypted = form.password.map(encryptionHelper.encrypt)
    Credential(username = form.screenName, hashSalt = encrypted.map(_.hashSalt), hashPassword = encrypted.map(_.hashPassword))
  }

  private def makeUser(form: SignUpForm) = User(
    screenName = form.screenName,
    firstName = form.firstName,
    lastName = form.lastName,
    gender = form.gender,
    primaryEmail = form.primaryEmail,
    avatarURL = ANONYMOUS_IMAGE_URL
  )

}
