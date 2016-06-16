package com.socialized.javascript.controllers

import org.scalajs.angularjs.AngularJsHelper._
import org.scalajs.angularjs.Timeout
import org.scalajs.angularjs._
import org.scalajs.angularjs.cookies.Cookies
import org.scalajs.angularjs.toaster.Toaster
import org.scalajs.angularjs.{Controller, Scope, injected}
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.models.InboxMessage
import com.socialized.javascript.services._
import org.scalajs.dom.browser.console

import scala.concurrent.duration._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Events Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class EventController($scope: EventControllerScope, $cookies: Cookies, $timeout: Timeout, toaster: Toaster,
                      @injected("InboxMessageService") messageService: InboxMessageService,
                      @injected("UserService") userService: UserService)
  extends Controller {

  $scope.messages = emptyArray
  $scope.jobsLoading = false
  $scope.messagesLoading = false

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.init = () => {
    val username = $cookies.getObject[String]("username") getOrElse "ldaniels"
    loadInboxMessages(username)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  private def loadInboxMessages(username: String) = {
    val startTime = System.currentTimeMillis()
    console.log("Loading inbox messages...")
    $scope.messagesLoading = true

    val outcome = for {
      user <- userService.getUserByName(username)
      userId = user._id getOrElse (throw new IllegalStateException("No user ID found"))
      messages <- messageService.getMessages(userId)
    } yield messages

    outcome onComplete {
      case Success(messages) =>
        $timeout(() => $scope.messagesLoading = false, 1.second)
        console.log(s"Loaded ${messages.length} job(s) in ${System.currentTimeMillis() - startTime} msec")
        $scope.messages = messages
      case Failure(e) =>
        $scope.messagesLoading = false
        toaster.error("Failed to retrieve inbox messages")
        console.error(s"Failed to retrieve inbox messages: ${e.displayMessage}")
    }
  }

}

/**
  * Event Controller Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait EventControllerScope extends Scope {
  var messages: js.Array[InboxMessage] = js.native

  var jobsLoading: js.UndefOr[Boolean] = js.native
  var interviewsLoading: js.UndefOr[Boolean] = js.native
  var messagesLoading: js.UndefOr[Boolean] = js.native

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  ///////////////////////////////////////////////////////////////////////////

  var init: js.Function0[Unit] = js.native

}

