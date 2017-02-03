package com.socialized.javascript.services

import io.scalajs.npm.angularjs.Service
import io.scalajs.npm.angularjs.http.Http
import io.scalajs.util.ScalaJsHelper._
import com.socialized.javascript.models.EntitySearchResult

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Reactive Search Service
  * @author lawrence.daniels@gmail.com
  */
class ReactiveSearchService($http: Http) extends Service {

  def search(searchTerm: String, maxResults: Int)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[EntitySearchResult]](s"/api/search?searchTerm=$searchTerm&maxResults=$maxResults")
  }

}
