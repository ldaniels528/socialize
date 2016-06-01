package com.socialized.forms

import java.util.Date

import com.socialized.models.Group
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Group Form
  * @author lawrence.daniels@gmail.com
  */
case class GroupForm(_id: Option[String],
                     name: Option[String],
                     avatarId: Option[String],
                     avatarURL: Option[String],
                     creationTime: Option[Date]) {

  /**
    * Transforms the group form into a group model
    * @return the [[Group group model]]
    */
  def toModel = Group(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    name = name,
    avatarId = avatarId,
    avatarURL = avatarURL,
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Group Form Companion
  * @author lawrence.daniels@gmail.com
  */
object GroupForm {

  /**
    * Group Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class GroupFormExtensions(val model: Group) extends AnyVal {

    /**
      * Transforms the group model into a group form
      * @return the [[GroupForm group form]]
      */
    def toForm = GroupForm(
      _id = model._id.map(_.stringify),
      name = model.name,
      avatarId = model.avatarId,
      avatarURL = model.avatarURL,
      creationTime = model.creationTime
    )

  }

  implicit val GroupFormFormat = Json.format[GroupForm]

}