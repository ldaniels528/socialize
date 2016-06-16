package com.socialized.javascript.controllers

import org.scalajs.angularjs.AngularJsHelper._
import org.scalajs.angularjs._
import org.scalajs.angularjs.toaster.Toaster
import org.scalajs.dom.browser.console
import com.socialized.javascript.controllers.home.SubmitterCacheService
import com.socialized.javascript.models.{Post, User}
import com.socialized.javascript.services.{MySessionService, PostService, UserService}
import com.socialized.javascript.ui.{Menu, MenuItem}

import scala.concurrent.duration._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Profile Controller (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class ProfileController($scope: ProfileControllerScope, $routeParams: ProfileRouteParams, $timeout: Timeout, toaster: Toaster,
                        @injected("MySession") mySession: MySessionService,
                        @injected("PostService") postService: PostService,
                        @injected("SubmitterCache") submitterCache: SubmitterCacheService,
                        @injected("UserService") userService: UserService)
  extends Controller {

  // setup the navigation menu
  $scope.menus = js.Array(
    Menu("MY PROFILE", items = js.Array(
      MenuItem.include(src = "/assets/views/home/navigation/profile_info.html")
    )),
    Menu("ACTIONS", items = js.Array(
      MenuItem.include(src = "/assets/views/profiles/menu/actions.html")
    ))
  )

  // retrieve the user and its postings
  $scope.profileID = $routeParams.id
  $scope.profileID.foreach(loadUserAndPostings)

  ///////////////////////////////////////////////////////////////////////////
  //      Blocking Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.block = (aUser: js.UndefOr[User]) => {
    alert("Not yet implemented")
  }

  $scope.isBlocked = (aUser: js.UndefOr[User]) => aUser map { user =>
    false
  }

  private def alert(message: String) {
    console.warn(message)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Contact Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.contact = (aUser: js.UndefOr[User]) => {
    alert("Not yet implemented")
  }

  $scope.isContacted = (aUser: js.UndefOr[User]) => aUser map { user =>
    false
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Endorsement Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.endorse = (aUser: js.UndefOr[User]) => {
    for {
      endorsee <- aUser
      endorseeID <- endorsee._id
      endorser <- mySession.user
      endorserID <- endorser._id
    } {
      $scope.endorseLoading = true
      userService.like(endorseeID, endorserID) onComplete {
        case Success(result) =>
          $timeout(() => $scope.endorseLoading = false, 500.millis)
          if (result.success) {
            endorsee.endorsements = if (endorsee.endorsements.isEmpty) 1: Integer else endorsee.endorsements.map(_ + 1)
            endorsee.endorsers.foreach(_.push(endorserID))
          }
        case Failure(e) =>
          $scope.endorseLoading = false
          console.error(e.displayMessage)
          toaster.error(e.displayMessage)
      }
    }
  }

  $scope.isEndorsed = (aUser: js.UndefOr[User]) => {
    for {
      user <- aUser
      endorser <- mySession.user
      endorsers <- user.endorsers
      endorserId <- endorser._id
    } yield endorsers.contains(endorserId)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Following Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.follow = (aUser: js.UndefOr[User]) => {
    for {
      followee <- aUser
      followeeID <- followee._id
      follower <- mySession.user
      followerID <- follower._id
    } {
      $scope.followLoading = true
      userService.follow(followeeID, followerID) onComplete {
        case Success(result) =>
          $timeout(() => $scope.followLoading = false, 500.millis)
          if (result.success) {
            followee.totalFollowers = if (followee.totalFollowers.isEmpty) 1: Integer else followee.totalFollowers.map(_ + 1)
            followee.followers.foreach(_.push(followerID))
          }
        case Failure(e) =>
          $scope.followLoading = false
          console.error(e.displayMessage)
          toaster.error(e.displayMessage)
      }
    }
  }

  $scope.isFollowed = (aUser: js.UndefOr[User]) => {
    for {
      user <- aUser
      follower <- mySession.user
      followers <- user.followers
      followerId <- follower._id
    } yield followers.contains(followerId)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Report Issue Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.report = (aUser: js.UndefOr[User]) => {
    alert("Not yet implemented")
  }

  $scope.isReported = (aUser: js.UndefOr[User]) => aUser map { user =>
    false
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Retrieve the user instance and its postings for the given user ID
    * @param userID the given user ID
    */
  private def loadUserAndPostings(userID: String) {
    console.log(s"Loading foreign user profile for $userID...")
    $scope.loadingStart()
    val outcome = for {
      user <- userService.getUserByID(userID)
      posts <- postService.getPostsByUserID(userID)
      enrichedPosts <- submitterCache.enrich(posts)
    } yield (user, enrichedPosts)

    outcome onComplete {
      case Success((user, posts)) =>
        $scope.loadingDelayedStop(1.second)
        $scope.profileUser = user
        $scope.posts = posts
      case Failure(e) =>
        $scope.loadingStop()
        console.error(e.displayMessage)
        toaster.error(e.displayMessage)
    }
  }

}

/**
  * Profile Controller Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait ProfileControllerScope extends Scope with GlobalLoadingScope {
  var menus: js.Array[Menu] = js.native
  var profileID: js.UndefOr[String] = js.native
  var profileUser: js.UndefOr[User] = js.native
  var posts: js.Array[Post] = js.native

  // blocking
  var block: js.Function1[js.UndefOr[User], Unit] = js.native
  var isBlocked: js.Function1[js.UndefOr[User], js.UndefOr[Boolean]] = js.native

  // contact
  var contact: js.Function1[js.UndefOr[User], Unit] = js.native
  var contactLoading: js.UndefOr[Boolean] = js.native
  var isContacted: js.Function1[js.UndefOr[User], js.UndefOr[Boolean]] = js.native

  // endorsements
  var endorse: js.Function1[js.UndefOr[User], Unit] = js.native
  var endorseLoading: js.UndefOr[Boolean] = js.native
  var isEndorsed: js.Function1[js.UndefOr[User], js.UndefOr[Boolean]] = js.native

  // followers
  var follow: js.Function1[js.UndefOr[User], Unit] = js.native
  var followLoading: js.UndefOr[Boolean] = js.native
  var isFollowed: js.Function1[js.UndefOr[User], js.UndefOr[Boolean]] = js.native

  // report
  var report: js.Function1[js.UndefOr[User], Unit] = js.native
  var reportLoading: js.UndefOr[Boolean] = js.native
  var isReported: js.Function1[js.UndefOr[User], js.UndefOr[Boolean]] = js.native

}

/**
  * Profile Controller Route Parameters
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait ProfileRouteParams extends js.Object {
  var id: js.UndefOr[String]
}