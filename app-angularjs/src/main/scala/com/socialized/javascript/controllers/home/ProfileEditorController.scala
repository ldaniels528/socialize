package com.socialized.javascript.controllers.home

import com.github.ldaniels528.meansjs.angularjs.AngularJsHelper._
import com.github.ldaniels528.meansjs.angularjs.{Timeout, _}
import com.github.ldaniels528.meansjs.angularjs.toaster.Toaster
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import com.socialized.javascript.forms.ProfileEditForm
import com.socialized.javascript.models.User
import com.socialized.javascript.services.{MySessionService, UserService}
import org.scalajs.dom
import com.github.ldaniels528.meansjs.core.console

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Profile Editor Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class ProfileEditorController($scope: ProfileEditorScope, $timeout: Timeout, toaster: Toaster,
                              @injected("MySession") mySession: MySessionService,
                              @injected("UserService") userService: UserService) extends Controller {

  ///////////////////////////////////////////////////////////////////////////
  //      Profile Editor Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.initProfileEditor = () => mySession.user foreach { user =>
    $scope.form = ProfileEditForm(user)
    console.log(s"Profit edit form => ${angular.toJson($scope.form)}")
  }

  $scope.updateProfile = (form: ProfileEditForm) => {
    userService.updateUser(form) onComplete {
      case Success(user) =>
        toaster.success("Profile Updated")
        mySession.user foreach { myUser =>
          myUser.firstName = user.firstName
          myUser.lastName = user.lastName
          myUser.primaryEmail = user.primaryEmail
          myUser.gender = user.gender
          myUser.company = user.company
          myUser.title = user.title
          myUser.avatarURL = user.avatarURL
          myUser.followers = user.followers
        }
        $scope.navigateToNewsFeed()
      case Failure(e) =>
        console.error(s"Failed to update user profile: ${e.displayMessage}")
        toaster.error("Profile Update Error", e.displayMessage)
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Event Listener Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.$on("user_loaded", (event: dom.Event, user: User) => {
    console.log(s"${getClass.getSimpleName}: user loaded - ${user.primaryEmail}")
    $scope.initProfileEditor()
  })

}

/**
  * Profile Editor Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait ProfileEditorScope extends HomeControllerScope {
  // properties
  var form: ProfileEditForm = js.native

  // functions
  var initProfileEditor: js.Function0[Unit] = js.native
  var updateProfile: js.Function1[ProfileEditForm, Unit] = js.native

}