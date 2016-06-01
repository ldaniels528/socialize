package com.socialized.javascript.models

import java.lang.{Boolean => JBoolean, Double => JDouble}

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a user model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class User(var _id: js.UndefOr[String] = js.undefined,
           var screenName: js.UndefOr[String] = js.undefined,
           var firstName: js.UndefOr[String] = js.undefined,
           var lastName: js.UndefOr[String] = js.undefined,
           var gender: js.UndefOr[String] = js.undefined,
           var title: js.UndefOr[String] = js.undefined,
           var level: js.UndefOr[JDouble] = js.undefined,
           var company: js.UndefOr[String] = js.undefined,
           var primaryEmail: js.UndefOr[String] = js.undefined,
           var avatarId: js.UndefOr[String] = js.undefined,
           var avatarURL: js.UndefOr[String] = js.undefined,
           var endorsements: js.UndefOr[Integer] = js.undefined,
           var endorsers: js.UndefOr[js.Array[String]] = js.undefined,
           var totalFollowers: js.UndefOr[Integer] = js.undefined,
           var followers: js.UndefOr[js.Array[String]] = js.undefined,
           var isAdmin: js.UndefOr[JBoolean] = js.undefined,
           var creationTime: js.UndefOr[js.Date] = js.undefined) extends js.Object

/**
  * User Companion Object
  * @author lawrence.daniels@gmail.com
  */
object User {

  /**
    * User Extensions
    * @param user the given [[User user]]
    */
  implicit class UserExtensions(val user: User) extends AnyVal {

    def fullName = Seq(user.firstName.toOption, user.lastName.toOption).flatten.mkString(" ")

  }

}