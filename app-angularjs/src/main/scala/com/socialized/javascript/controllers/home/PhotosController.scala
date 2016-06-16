package com.socialized.javascript.controllers.home

import org.scalajs.angularjs.AngularJsHelper._
import org.scalajs.angularjs.{Timeout, _}
import org.scalajs.angularjs.toaster.Toaster
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.models.{Attachment, User}
import com.socialized.javascript.services.{MySessionService, PostService}
import org.scalajs.dom
import org.scalajs.dom.browser.console

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Photos Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class PhotosController($scope: PhotosScope, $timeout: Timeout, toaster: Toaster,
                       @injected("MySession") mySession: MySessionService,
                       @injected("PostService") postService: PostService) extends Controller {

  $scope.photos = emptyArray

  ///////////////////////////////////////////////////////////////////////////
  //      Public Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.loadPhotos = () => {
    for {
      user <- mySession.user
      userID <- user._id
    } {
      console.log(s"Loading photos for user ${user.screenName} (${user._id})...")
      postService.getAttachmentsByUserID(userID) onComplete {
        case Success(photos) =>
          $scope.photos = photos
        case Failure(e) =>
          console.error(s"Failed to retrieve photos: ${e.displayMessage}")
          toaster.error("Loading Error", "General fault while retrieving photos")
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Event Listener Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.$on("user_loaded", (event: dom.Event, user: User) => {
    console.log(s"${getClass.getSimpleName}: user loaded - ${user.primaryEmail}")
    $scope.loadPhotos()
  })

}

@js.native
trait PhotosScope extends Scope {
  // properties
  var photos: js.Array[Attachment] = js.native

  // functions
  var loadPhotos: js.Function0[Unit] = js.native

}