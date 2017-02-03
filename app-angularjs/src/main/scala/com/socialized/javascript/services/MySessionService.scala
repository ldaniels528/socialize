package com.socialized.javascript.services

import com.socialized.javascript.models.{Session, User, UserLike}
import com.socialized.javascript.services.MySessionService.{INITIAL_GRACE_PERIOD, SESSION_COOKIE_NAME}
import io.scalajs.npm.angularjs.AngularJsHelper._
import io.scalajs.npm.angularjs._
import io.scalajs.npm.angularjs.cookies.Cookies
import io.scalajs.npm.angularjs.toaster._
import io.scalajs.dom.html.browser.console
import io.scalajs.util.JsUnderOrHelper._

import scala.concurrent.duration._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

/**
  * My Session Service
  * @author lawrence.daniels@gmail.com
  */
class MySessionService($rootScope: Scope, $cookies: Cookies, $location: Location, $timeout: Timeout, toaster: Toaster,
                       @injected("UserService") userSvc: UserService,
                       @injected("UserSessionService") sessionSvc: UserSessionService,
                       @injected("WebSocketService") webSocketSvc: WebSocketService) extends Service {

  var onlineStatuses = js.Dictionary[Session]()
  var mySession: js.UndefOr[Session] = js.undefined
  var myUser: js.UndefOr[User] = js.undefined
  val initTime = System.currentTimeMillis()

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Retrieves the last updated time for the session for the given user
    * @param user the given [[com.socialized.javascript.models.UserLike user]]
    * @return the [[js.Date last updated time]] for the session
    */
  def getSessionLastUpdatedTime(user: UserLike) = {
    for {
      id <- user._id
      session <- onlineStatuses.get(id).orUndefined
      lastUpdated <- session.lastUpdated
    } yield lastUpdated
  }

  /**
    * Returns the current session
    * @return the current session
    */
  def session: js.UndefOr[Session] = {
    if (mySession.isDefined) mySession
    else {
      if (elapsedTime > INITIAL_GRACE_PERIOD) returnToLoginPage()
      js.undefined
    }
  }

  /**
    * Returns the currently authentication user
    * @return the currently authentication user
    */
  def user: js.UndefOr[User] = {
    if (mySession.isDefined) myUser
    else {
      if (elapsedTime > INITIAL_GRACE_PERIOD) returnToLoginPage()
      js.undefined
    }
  }

  /**
    * Loads the appropriate user for the given session
    * @param loadedSession the given [[com.socialized.javascript.models.Session session]]
    */
  def loadUserForSession(loadedSession: Session) {
    myUser = js.undefined
    announceSession(loadedSession)

    // is the session authorized? if so, load the user's profile
    session.flatMap(_.userID.flat).toOption match {
      case Some(userID) =>
        userSvc.getUserByID(userID) onComplete {
          case Success(loadedUser) => announceUser(loadedUser)
          case Failure(e) =>
            console.error(s"Failed to retrieve user: ${e.displayMessage}")
        }
      case None =>
        returnToLoginPage()
    }
  }

  /**
    * Performs a system logout
    */
  def logout() {
    $cookies.remove(SESSION_COOKIE_NAME)
    mySession = js.undefined
    myUser = js.undefined
    returnToLoginPage()
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  private def announceSession(loadedSession: Session) = {
    console.log(s"Session ${loadedSession._id} (${loadedSession.primaryEmail}) loaded in $elapsedTime msec")
    mySession = loadedSession
    setSessionCookie(loadedSession)
    $rootScope.$broadcast("session_loaded", loadedSession)
  }

  private def announceUser(loadedUser: User) = {
    console.log(s"User ${loadedUser.primaryEmail} loaded in $elapsedTime msec")
    myUser = loadedUser
    webSocketSvc.init(loadedUser)

    val userID = loadedUser._id getOrElse (throw new IllegalStateException("User ID not found"))
    updateOnlineStatusesForFollowers(userID)
    $rootScope.$broadcast("user_loaded", loadedUser)
  }

  /**
    * Returns the elapsed time in milliseconds since the service started
    * @return the elapsed time in milliseconds
    */
  private def elapsedTime = System.currentTimeMillis() - initTime

  /**
    * Loads the session and user
    */
  private def loadSessionAndUser() {
    $cookies.get[String](SESSION_COOKIE_NAME).toOption match {
      case Some(sessionId) =>
        console.log(s"Attempting to load user session $sessionId...")
        val outcome = for {
          session <- sessionSvc.getSession(sessionId)
          userID = session.userID getOrElse (throw new IllegalStateException("No user ID specified"))
          user <- userSvc.getUserByID(userID)
        } yield (session, user)

        outcome onComplete {
          case Success((loadedSession, loadedUser)) =>
            announceSession(loadedSession)
            announceUser(loadedUser)
          case Failure(e) =>
            console.error(s"Failed to retrieve session: ${e.displayMessage}")
            toaster.warning("Session Not Found", "Session could not be retrieved")
            returnToLoginPage()
        }
      case None =>
        returnToLoginPage()
    }
  }

  private def updateOnlineStatusesForFollowers(userID: String): Unit = {
    for {
      thisSession <- mySession
      userID <- thisSession.userID
    } {
      console.log("Updating the online status for all followers...")
      val outcome = for {
        followers <- userSvc.getFollowers(userID)
        sessions <- sessionSvc.getSessions(followers.flatMap(_._id.toOption))
      } yield sessions

      outcome onComplete {
        case Success(sessions) =>
          onlineStatuses = js.Dictionary(sessions.map { session => (userID, session) }: _*)
          $timeout(() => updateOnlineStatusesForFollowers(userID), 5.minutes)
        case Failure(e) =>
          console.error(s"Failed to retrieve online statuses: ${e.displayMessage}")
      }
    }
  }

  private def returnToLoginPage() {
    mySession = js.undefined
    myUser = js.undefined
    $cookies.remove(SESSION_COOKIE_NAME)
    $location.url("/login")
  }

  private def setSessionCookie(loadedSession: Session) = {
    loadedSession._id.toOption match {
      case Some(sessionId) =>
        console.log(s"Setting cookie for session $sessionId")
        $cookies.put(SESSION_COOKIE_NAME, sessionId)
      case None =>
        console.warn(s"No session cookie was set - ${angular.toJson(loadedSession)}")
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Initialization
  ///////////////////////////////////////////////////////////////////////////

  // initialize by loading the session
  loadSessionAndUser()

}

/**
  * My Session Service Companion
  * @author lawrence.daniels@gmail.com
  */
object MySessionService {
  val SESSION_COOKIE_NAME = "SKILLDIAL_session_id"
  val INITIAL_GRACE_PERIOD = 30000L

}
