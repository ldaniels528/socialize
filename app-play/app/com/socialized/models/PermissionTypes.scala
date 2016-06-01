package com.socialized.models

import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}
import reactivemongo.bson.{BSONHandler, BSONString}

/**
  * Represents an enumeration of Permissions
  * @author lawrence.daniels@gmail.com
  */
object PermissionTypes extends Enumeration {
  type PermissionType = Value

  val FOLLOWERS = Value("FOLLOWERS")
  //val GROUP = Value("GROUP") TODO eventual groups and specific individuals should be supported
  val PRIVATE = Value("PRIVATE")
  val PUBLIC = Value("PUBLIC")

  /**
    * Permission JSON Format
    * @author lawrence.daniels@gmail.com
    */
  implicit object PermissionFormat extends Format[PermissionType] {

    def reads(json: JsValue) = JsSuccess(PermissionTypes.withName(json.as[String]))

    def writes(permission: PermissionType) = JsString(permission.toString)
  }

  /**
    * Permission BSON Handler
    * @author lawrence.daniels@gmail.com
    */
  implicit object PermissionHandler extends BSONHandler[BSONString, PermissionType] {

    def read(string: BSONString) = PermissionTypes.withName(string.value)

    def write(permission: PermissionType) = BSONString(permission.toString)
  }

}
