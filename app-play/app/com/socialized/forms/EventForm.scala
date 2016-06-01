package com.socialized.forms

import java.util.Date

import com.socialized.models.{Event, PermissionTypes, Submitter}
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Event Form
  * @author lawrence.daniels@gmail.com
  */
case class EventForm(_id: Option[String],
                     `type`: Option[String],
                     title: Option[String],
                     permissions: Option[String],
                     owner: Option[Submitter],
                     eventTime: Option[Date],
                     creationTime: Option[Date]) {

  /**
    * Transforms the event form into a event model
    * @return the [[Event event model]]
    */
  def toModel = Event(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    `type` = `type`,
    title = title,
    permissions = permissions.map(PermissionTypes.withName),
    owner = owner,
    eventTime = eventTime ?? new Date(),
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Event Form Companion
  * @author lawrence.daniels@gmail.com
  */
object EventForm {

  /**
    * Event Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class EventFormExtensions(val model: Event) extends AnyVal {

    /**
      * Transforms the event model into a event form
      * @return the [[EventForm event form]]
      */
    def toForm = EventForm(
      _id = model._id.map(_.stringify) ?? BSONObjectID.generate.stringify,
      `type` = model.`type`,
      title = model.title,
      permissions = model.permissions.map(_.toString),
      owner = model.owner,
      eventTime = model.eventTime ?? new Date(),
      creationTime = model.creationTime ?? new Date()
    )

  }

  implicit val EventFormFormat = Json.format[EventForm]

}