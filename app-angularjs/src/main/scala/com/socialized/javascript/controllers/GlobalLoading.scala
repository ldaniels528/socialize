package com.socialized.javascript.controllers

import io.scalajs.npm.angularjs.{Controller, Scope, Timeout}
import io.scalajs.util.JsUnderOrHelper._

import scala.scalajs.js

/**
  * Global Loading Feature
  * @author lawrence.daniels@gmail.com
  */
trait GlobalLoading {
  self: Controller =>

  def $scope: GlobalLoadingScope

  def $timeout: Timeout

  ///////////////////////////////////////////////////////////////////////////
  //      Global Loading Functions
  ///////////////////////////////////////////////////////////////////////////

  $scope.isLoading = () => $scope.loading.contains(true)

  $scope.loadingDelayedStop = (delay: Int) => {
    $timeout(() => $scope.loading = false, delay)
  }

  $scope.loadingStart = () => {
    $scope.loading = true
  }

  $scope.loadingStop = () => {
    $scope.loading = false
  }

}

/**
  * Global Loading Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait GlobalLoadingScope extends Scope {
  var loading: js.UndefOr[Boolean] = js.native

  var isLoading: js.Function0[Boolean] = js.native
  var loadingDelayedStop: js.Function1[Int, js.Promise[js.Any]] = js.native
  var loadingStart: js.Function0[Unit] = js.native
  var loadingStop: js.Function0[Unit] = js.native

}