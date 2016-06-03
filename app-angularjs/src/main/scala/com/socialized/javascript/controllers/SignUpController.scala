package com.socialized.javascript.controllers

import com.github.ldaniels528.meansjs.angularjs.AngularJsHelper._
import com.github.ldaniels528.meansjs.angularjs.{Location, Timeout}
import com.github.ldaniels528.meansjs.angularjs.toaster.Toaster
import com.github.ldaniels528.meansjs.angularjs.{Controller, Scope, injected}
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import com.socialized.javascript.forms.SignUpForm
import com.socialized.javascript.services.{MySessionService, SignUpService}
import com.github.ldaniels528.meansjs.core.browser.console

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Sign-Up Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class SignUpController($scope: SignUpControllerScope, $location: Location, $timeout: Timeout, toaster: Toaster,
                       @injected("MySession") mySession: MySessionService,
                       @injected("SignUpService") signUpSvc: SignUpService)
  extends Controller {

  $scope.form = new SignUpForm()
  $scope.messages = emptyArray
  $scope.signupLoading = false

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  //////////////////////////////////////////////////////////////////////////

  $scope.signUp = (form: SignUpForm) => {
    if (validate(form)) {
      $scope.signupLoading = true
      signUpSvc.createAccount(form) onComplete {
        case Success(session) =>
          console.log("Sign-up successful... ")
          mySession.loadUserForSession(session)
          $scope.signupLoading = false
          $location.path("/home")
        case Failure(e) =>
          $scope.signupLoading = false
          console.log(e.displayMessage)
          $scope.messages.push(e.displayMessage)
      }
    }
    ()
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  private def validate(form: SignUpForm) = {
    $scope.messages.removeAll()
    if (!form.screenName.exists(_.trim.nonEmpty)) $scope.messages.push("Screen name is required")
    if (!form.password.exists(_.trim.nonEmpty)) $scope.messages.push("Password is required")
    if (!form.primaryEmail.exists(_.trim.nonEmpty)) $scope.messages.push("Email address is required")
    if (form.primaryEmail.exists(s => s.trim.nonEmpty && !s.isValidEmail)) $scope.messages.push("Email Address is invalid")
    $scope.messages.isEmpty
  }

}

/**
  * Authentication Controller Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait SignUpControllerScope extends Scope {
  var form: SignUpForm = js.native
  var messages: js.Array[String] = js.native
  var signupLoading: Boolean = js.native

  // Functions
  var signUp: js.Function1[SignUpForm, Unit] = js.native

}
