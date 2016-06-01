package com.socialized.forms

import com.socialized.util.StringHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.Macros

/**
  * Represents a sign-up (registration) form
  * @author lawrence.daniels@gmail.com
  */
case class SignUpForm(screenName: Option[String],
                      password: Option[String],
                      firstName: Option[String],
                      lastName: Option[String],
                      primaryEmail: Option[String],
                      gender: Option[String]) {

  def validate = {
    var messages: List[String] = Nil
    if (screenName.exists(_.trim.isEmpty)) messages = "Username is missing" :: messages
    if (password.exists(_.trim.isEmpty)) messages = "Password is missing" :: messages
    if (primaryEmail.exists(_.trim.isEmpty)) messages = "Email Address is missing" :: messages
    if (!primaryEmail.exists(s => s.trim.nonEmpty && s.isValidEmail)) messages = "Email Address is invalid" :: messages
    messages
  }

}

/**
  * Sign-Up Form Companion
  * @author lawrence.daniels@gmail.com
  */
object SignUpForm {

  implicit val SignUpFormReads = Json.reads[SignUpForm]

  implicit val SignUpFormWrites = Json.writes[SignUpForm]

  implicit val SignUpFormReader = Macros.reader[SignUpForm]

  implicit val SignUpFormWriter = Macros.writer[SignUpForm]

}