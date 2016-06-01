package com.socialized.javascript.routes

import com.github.ldaniels528.meansjs.nodejs.bcrypt.BCrypt
import com.github.ldaniels528.meansjs.nodejs.express.{Application, Request, Response}
import com.github.ldaniels528.meansjs.nodejs.mongodb._
import com.github.ldaniels528.meansjs.nodejs.{Require, console}
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import com.socialized.javascript.data.CredentialDAO._
import com.socialized.javascript.data.{CredentialDAO, CredentialData, SessionDAO, UserDAO}
import com.socialized.javascript.data.SessionDAO._
import com.socialized.javascript.data.UserDAO._
import com.socialized.javascript.models.{Session, User}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Authentication Routes
  * @author lawrence.daniels@gmail.com
  */
object AuthenticationRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext, require: Require, mongo: MongoDB) = {
    val credentialDAO = dbFuture.flatMap(_.getCredentialDAO)
    val sessionDAO = dbFuture.flatMap(_.getSessionDAO)
    val userDAO = dbFuture.flatMap(_.getUserDAO)
    implicit val bCrypt = require[BCrypt]("bcrypt")

    app.post("/api/signin", (request: Request, response: Response, next: NextFunction) => signIn(request, response, next)(credentialDAO, sessionDAO, userDAO))
  }

  /**
    * Authentication a user
    * @example POST /api/signin <= { "screenName":"ldaniels", "password":"$sadwqwe$%#" }
    */
  def signIn(request: Request, response: Response, next: NextFunction)(credentialDAO: Future[CredentialDAO], sessionDAO: Future[SessionDAO], userDAO: Future[UserDAO])(implicit ec: ExecutionContext, bCrypt: BCrypt, mongo: MongoDB) = {
    // get the JSON body as a login form instance
    request.bodyAs[LoginForm] match {
      case form if form.screenName.isEmpty => response.badRequest("The screen name is required"); next()
      case form if form.password.isEmpty => response.badRequest("The password is required"); next()
      case form =>
        val (screenName, password) = (form.screenName.orNull, form.password.orNull)

        // lookup the user and its credentials
        val outcome = for {
          credentialOpt <- credentialDAO.flatMap(_.findOneFuture[CredentialData]("username" $eq screenName))
          userOpt <- userDAO.flatMap(_.findOneFuture[User]("screenName" $eq screenName))
          sessionOpt <- login(sessionDAO, credentialOpt, userOpt, screenName, password)
        } yield sessionOpt

        outcome onComplete {
          case Success(Some(session)) => response.send(session); next()
          case Success(None) => response.badRequest("Either the username or password was invalid"); next()
          case Failure(e) => response.internalServerError(e); next()
        }
    }
  }

  private def login(sessionDAO: Future[Collection],
                    credentialOpt: Option[CredentialData],
                    userOpt: Option[User],
                    screenName: String,
                    password: String)(implicit ec: ExecutionContext, bCrypt: BCrypt): Future[Option[Session]] = {
    (for {
      credential <- credentialOpt
      user <- userOpt
    } yield {
      if (authenticate(credential, screenName, password))
        sessionDAO.flatMap(_.insert(toSession(user))) map (_.opsAs[Session].headOption)
      else
        Future.successful(None)
    }) getOrElse Future.successful(None)
  }

  /**
    * Performs the actual authentication task
    * @param screenName the given user/screen name
    * @param password   the given password
    * @return a promise of an authenticated (true) or non-authenticated (false) result
    */
  private def authenticate(credential: CredentialData, screenName: String, password: String)(implicit ec: ExecutionContext, bCrypt: BCrypt): Boolean = {
    val matched = bCrypt.compareSync(password, credential.hashPassword)
    if (!matched) console.error(s"User '$screenName' password didn't match") else console.log(s"User '$screenName' password matched")
    matched
  }

  private def toSession(user: User) = Session(
    userID = user._id,
    screenName = user.screenName,
    primaryEmail = user.primaryEmail,
    avatarURL = user.avatarURL,
    isAnonymous = false
  )

  /**
    * Login Form
    * @author lawrence.daniels@gmail.com
    */
  @js.native
  trait LoginForm extends js.Object {
    var screenName: js.UndefOr[String] = js.native
    var password: js.UndefOr[String] = js.native
  }

}
