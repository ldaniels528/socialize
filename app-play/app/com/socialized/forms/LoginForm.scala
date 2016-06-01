package com.socialized.forms

import play.api.libs.json.Json
import reactivemongo.bson.Macros

/**
  * Represents a log-in (authentication) form
  * @author lawrence.daniels@gmail.com
  */
case class LoginForm(screenName: Option[String], password: Option[String])

/**
  * Sign-Up Form Companion
  * @author lawrence.daniels@gmail.com
  */
object LoginForm {

  implicit val LoginFormReads = Json.reads[LoginForm]

  implicit val LoginFormWrites = Json.writes[LoginForm]

  implicit val LoginFormReader = Macros.reader[LoginForm]

  implicit val LoginFormWriter = Macros.writer[LoginForm]

}