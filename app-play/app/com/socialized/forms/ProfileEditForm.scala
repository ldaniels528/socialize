package com.socialized.forms

import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson.{BSONObjectID, Macros}

/**
  * Profile Edit Form
  * @author lawrence.daniels@gmail.com
  */
case class ProfileEditForm(_id: Option[BSONObjectID],
                           firstName: Option[String],
                           lastName: Option[String],
                           gender: Option[String],
                           title: Option[String],
                           company: Option[String],
                           primaryEmail: Option[String],
                           avatarURL: Option[String])


/**
  * Profile Edit Form Companion
  * @author lawrence.daniels@gmail.com
  */
object ProfileEditForm {

  implicit val ProfileEditFormReads = Json.reads[ProfileEditForm]

  implicit val ProfileEditFormWrites = Json.writes[ProfileEditForm]

  implicit val ProfileEditFormReader = Macros.reader[ProfileEditForm]

  implicit val ProfileEditFormWriter = Macros.writer[ProfileEditForm]

}
