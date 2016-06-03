package com.socialized.javascript.controllers

import com.github.ldaniels528.meansjs.angularjs.{Controller, Location, Scope}
import com.github.ldaniels528.meansjs.core.browser.console

import scala.scalajs.js

/**
  * Global Navigation Feature
  * @author lawrence.daniels@gmail.com
  */
trait GlobalNavigation {
  self: Controller =>

  def $scope: GlobalNavigationScope

  def $location: Location

  ///////////////////////////////////////////////////////////////////////////
  //      Home Navigation Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.navigateToEvents = () => navigateToPath("/home/events")

  $scope.navigateToMessages = () => navigateToPath("/home/messages")

  $scope.navigateToNewsFeed = () => navigateToPath("/home/newsfeed")

  $scope.navigateToOrganizationViewer = (aId: js.UndefOr[String]) => aId.foreach(id =>navigateToPath(s"/home/organization/viewer/$id"))

  $scope.navigateToPhotos = () => navigateToPath("/home/photos")

  $scope.navigateToProfileEditor = () => navigateToPath("/home/profile/edit")

  $scope.navigateToProfileViewer = (aId: js.UndefOr[String]) => aId.foreach(id => navigateToPath(s"/home/profile/viewer/$id"))

  ///////////////////////////////////////////////////////////////////////////
  //      Test Navigation Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.navigateToCreateTest = () => navigateToPath("/skills/tests/create")

  $scope.navigateToEditTest = (aExamID: js.UndefOr[String]) => aExamID.foreach(examID => navigateToPath(s"/skills/tests/edit/$examID"))

  $scope.navigateToTakeTest = (aExamID: js.UndefOr[String]) => aExamID.foreach(examID => navigateToPath(s"/skills/tests/take/$examID"))

  $scope.navigateToTestDetails = (aExamID: js.UndefOr[String]) => aExamID.foreach(examID => navigateToPath(s"/skills/tests/details/$examID"))

  ///////////////////////////////////////////////////////////////////////////
  //      Private Functions
  ///////////////////////////////////////////////////////////////////////////

  private def navigateToPath(url: String): Unit = {
    console.log(s"Navigating to '$url'...")
    $location.path(url)
  }

}

/**
  * Global Navigation Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait GlobalNavigationScope extends Scope {
  // home
  var navigateToEvents: js.Function0[Unit] = js.native
  var navigateToMessages: js.Function0[Unit] = js.native
  var navigateToNewsFeed: js.Function0[Unit] = js.native
  var navigateToOrganizationViewer: js.Function1[js.UndefOr[String], Unit] = js.native
  var navigateToPhotos: js.Function0[Unit] = js.native
  var navigateToProfileEditor: js.Function0[Unit] = js.native
  var navigateToProfileViewer: js.Function1[js.UndefOr[String], Unit] = js.native

  // exam
  var navigateToCreateTest: js.Function0[Unit] = js.native
  var navigateToEditTest: js.Function1[js.UndefOr[String], Unit] = js.native
  var navigateToTakeTest: js.Function1[js.UndefOr[String], Unit] = js.native
  var navigateToTestDetails: js.Function1[js.UndefOr[String], Unit] = js.native

}