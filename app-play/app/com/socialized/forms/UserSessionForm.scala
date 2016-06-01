package com.socialized.forms

import java.util.Date

import com.socialized.controllers.ApplicationConstants._
import com.socialized.models.UserSession
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Represents an ephemeral User Session
  * @author lawrence.daniels@gmail.com
  */
case class UserSessionForm(_id: Option[String],
                           userID: Option[String],
                           screenName: Option[String],
                           primaryEmail: Option[String],
                           avatarURL: Option[String],
                           isAnonymous: Option[Boolean],
                           creationTime: Option[Date],
                           lastUpdated: Option[Date]) {

  def toModel = new UserSession(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    userID = userID.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    screenName = screenName,
    primaryEmail = primaryEmail,
    avatarURL = avatarURL.getOrElse(ANONYMOUS_IMAGE_URL),
    isAnonymous = isAnonymous.contains(true),
    creationTime = creationTime ?? new Date(),
    lastUpdated = lastUpdated ?? new Date()
  )

}

/**
  * User Session Form
  * @author lawrence.daniels@gmail.com
  */
object UserSessionForm {

  /**
    * User Session Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class UserSessionFormExtensions(val model: UserSession) extends AnyVal {

    def toForm = new UserSessionForm(
      _id = model._id.map(_.stringify),
      userID = model.userID.map(_.stringify),
      screenName = model.screenName,
      primaryEmail = model.primaryEmail,
      avatarURL = model.avatarURL,
      isAnonymous = model.isAnonymous,
      creationTime = model.creationTime,
      lastUpdated = model.lastUpdated
    )

  }

  implicit val UserSessionFormFormat = Json.format[UserSessionForm]

}