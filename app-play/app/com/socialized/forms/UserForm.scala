package com.socialized.forms

import java.util.Date

import com.socialized.models.User
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * User Form
  * @author lawrence.daniels@gmail.com
  */
case class UserForm(_id: Option[String],
                    screenName: Option[String],
                    firstName: Option[String] = None,
                    lastName: Option[String] = None,
                    gender: Option[String] = None,
                    title: Option[String] = None,
                    level: Option[Double] = None,
                    company: Option[String] = None,
                    primaryEmail: Option[String],
                    avatarId: Option[String] = None,
                    avatarURL: Option[String] = None,
                    endorsements: Option[Int] = 0,
                    endorsers: Option[List[String]] = Nil,
                    totalFollowers: Option[Int] = 0,
                    followers: Option[List[String]] = Nil,
                    isAdmin: Option[Boolean] = None,
                    creationTime: Option[Date]) {

  /**
    * Transforms the user form into a user model
    * @return the [[User user model]]
    */
  def toModel = User(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    screenName = screenName,
    firstName = firstName,
    lastName = lastName,
    gender = gender,
    title = title,
    level = level ?? 1d,
    company = company,
    primaryEmail = primaryEmail,
    avatarId = avatarId,
    avatarURL = avatarURL,
    endorsements = endorsements ?? 0,
    endorsers = endorsers ?? Nil,
    totalFollowers = totalFollowers ?? 0,
    followers = followers ?? Nil,
    isAdmin = isAdmin,
    creationTime = creationTime ?? new Date()
  )

}

/**
  * User Form Companion
  * @author lawrence.daniels@gmail.com
  */
object UserForm {

  /**
    * User Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class UserFormExtensions(val model: User) extends AnyVal {

    /**
      * Transforms the user model into a user form
      * @return the [[UserForm user form]]
      */
    def toForm = UserForm(
      _id = model._id.map(_.stringify) ?? BSONObjectID.generate.stringify,
      screenName = model.screenName,
      firstName = model.firstName,
      lastName = model.lastName,
      gender = model.gender,
      title = model.title,
      level = model.level ?? 1d,
      company = model.company,
      primaryEmail = model.primaryEmail,
      avatarId = model.avatarId,
      avatarURL = model.avatarURL,
      endorsements = model.endorsements ?? 0,
      endorsers = model.endorsers ?? Nil,
      totalFollowers = model.totalFollowers ?? 0,
      followers = model.followers ?? Nil,
      isAdmin = model.isAdmin,
      creationTime = model.creationTime ?? new Date()
    )

  }

  implicit val UserFormFormat = Json.format[UserForm]

}