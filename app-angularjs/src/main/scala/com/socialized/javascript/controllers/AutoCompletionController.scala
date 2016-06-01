package com.socialized.javascript.controllers

import com.github.ldaniels528.meansjs.angularjs.AngularJsHelper._
import com.github.ldaniels528.meansjs.angularjs.{Controller, Q, Scope}
import com.socialized.javascript.models.EntitySearchResult
import com.socialized.javascript.services.ReactiveSearchService

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * Represents a abstract controller with Auto Completion capability
  * @author lawrence.daniels@gmail.com
  */
abstract class AutoCompletionController($scope: AutoCompletionScope, $q: Q, reactiveSearchSvc: ReactiveSearchService) extends Controller {

  $scope.autoCompleteSearch = (searchTerm: String) => {
    val deferred = $q.defer[js.Array[EntitySearchResult]]()
    reactiveSearchSvc.search(searchTerm, maxResults = 20) onComplete {
      case Success(response) => deferred.resolve(response)
      case Failure(e) => deferred.reject(e.displayMessage)
    }
    deferred.promise
  }

}

/**
  * Auto Completion Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait AutoCompletionScope extends Scope {
  var autoCompleteSearch: js.Function1[String, js.Promise[js.Array[EntitySearchResult]]] = js.native

}
