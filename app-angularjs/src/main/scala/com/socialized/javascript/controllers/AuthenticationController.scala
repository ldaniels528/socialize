package com.socialized.javascript.controllers

import com.github.ldaniels528.meansjs.angularjs.AngularJsHelper._
import com.github.ldaniels528.meansjs.angularjs.{Controller, Location, Scope, injected}
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import com.socialized.javascript.forms.LoginForm
import com.socialized.javascript.services.{AuthenticationService, MySessionService}
import com.github.ldaniels528.meansjs.core.browser.console

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Authentication Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class AuthenticationController($scope: AuthenticationControllerScope, $location: Location,
                               @injected("AuthenticationService") authenticationService: AuthenticationService,
                               @injected("MySession") mySession: MySessionService)
  extends Controller {

  $scope.form = LoginForm()
  $scope.loginLoading = false
  $scope.messages = emptyArray

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.login = (form: LoginForm) => {
    $scope.messages = form.validate
    if ($scope.messages.isEmpty) {
      $scope.loginLoading = true
      authenticationService.authenticate(form) onComplete {
        case Success(session) =>
          console.log("Login successful... ")
          mySession.loadUserForSession(session)
          $scope.loginLoading = false
          $location.path("/home")
        case Failure(e) =>
          $scope.loginLoading = false
          $scope.messages.push(e.displayMessage)
          console.log(e.displayMessage)
      }
    }
    ()
  }

}

/**
  * Authentication Controller Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait AuthenticationControllerScope extends Scope {
  var messages: js.Array[String] = js.native
  var loginLoading: js.UndefOr[Boolean] = js.native
  var form: LoginForm = js.native

  // Functions
  var login: js.Function1[LoginForm, Unit] = js.native

}

