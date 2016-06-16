package com.socialized.javascript.services

import org.scalajs.angularjs.Service
import org.scalajs.angularjs.http.Http
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.models.Group

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Groups Service
  * @author lawrence.daniels@gmail.com
  */
class GroupService($http: Http) extends Service {

  /**
    * Retrieves a group by ID
    * @param groupID the given group ID
    * @return a promise of a [[com.socialized.javascript.models.Group group]]
    */
  def getGroupByID(groupID: String)(implicit ec: ExecutionContext) = {
    $http.get[Group](s"/api/group/$groupID")
  }

  /**
    * Retrieves a group via name (e.g. "Scala.Js Developers")
    * @return a promise of a [[com.socialized.javascript.models.Group group]]
    */
  def getGroupByName(name: String)(implicit ec: ExecutionContext) = {
    $http.get[Group](s"/api/group?name=$name")
  }

  /**
    * Retrieves all groups
    * @param maxResults the maximum number of results to return
    * @return a promise of an array of [[com.socialized.javascript.models.Group groups]]
    */
  def getGroups(maxResults: Int = 20)(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Group]](s"/api/groups?maxResults=$maxResults")
  }

}
