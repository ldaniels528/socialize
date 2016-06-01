package com.socialized.models

import java.util.Date

import com.socialized.controllers.ApplicationConstants._
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson._
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}

/**
  * Represents an ephemeral User Session
  * @author lawrence.daniels@gmail.com
  */
case class UserSession(_id: Option[BSONObjectID] = BSONObjectID.generate,
                       userID: Option[BSONObjectID],
                       screenName: Option[String],
                       primaryEmail: Option[String],
                       avatarURL: String,
                       isAnonymous: Boolean = false,
                       creationTime: Option[Date] = new Date(),
                       lastUpdated: Option[Date] = new Date())

/**
  * User Session Companion
  * @author lawrence.daniels@gmail.com
  */
object UserSession {

  def toSession(user: User) = new UserSession(
    userID = user._id,
    screenName = user.screenName,
    primaryEmail = user.primaryEmail,
    avatarURL = user.avatarURL.getOrElse(ANONYMOUS_IMAGE_URL),
    isAnonymous = false
  )

  implicit val UserSessionFormat = Json.format[UserSession]

  implicit val UserSessionHandler = Macros.handler[UserSession]

}