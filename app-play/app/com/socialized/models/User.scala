package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper.Implicits._
import play.api.libs.json.Json
import reactivemongo.bson.{BSONObjectID, Macros}
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}

/**
  * Represents a User model object
  * @author lawrence.daniels@gmail.com
  */
case class User(_id: Option[BSONObjectID] = BSONObjectID.generate,
                screenName: Option[String],
                firstName: Option[String] = None,
                lastName: Option[String] = None,
                gender: Option[String] = None,
                title: Option[String] = None,
                level: Option[Double] = 1d,
                company: Option[String] = None,
                primaryEmail: Option[String],
                avatarId: Option[String] = None,
                avatarURL: Option[String] = None,
                endorsements: Option[Int] = 0,
                endorsers: Option[List[String]] = Nil,
                totalFollowers: Option[Int] = 0,
                followers: Option[List[String]] = Nil,
                isAdmin: Option[Boolean] = None,
                creationTime: Option[Date] = new Date())

/**
  * User Singleton
  * @author lawrence.daniels@gmail.com
  */
object User {

  implicit val UserFormat = Json.format[User]

  implicit val UserHandler = Macros.handler[User]

  /**
    * Syntactic sugar for returning a pre-formatted name and credentials string
    * @param user the given [[User user]]
    */
  implicit class UserEnrichment(val user: User) extends AnyVal {

    def fullName: Option[String] = s"${user.firstName.getOrElse("")} ${user.lastName.getOrElse("")}".trim

  }

}