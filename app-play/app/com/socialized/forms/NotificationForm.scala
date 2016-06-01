package com.socialized.forms

import java.util.Date

import com.socialized.models.{Notification, Submitter}
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Notification form
  * @author lawrence.daniels@gmail.com
  */
case class NotificationForm(_id: Option[String],
                            `type`: Option[String],
                            title: Option[String],
                            text: Option[String],
                            owner: Option[Submitter],
                            eventTime: Option[Date],
                            readTime: Option[Date],
                            creationTime: Option[Date]) {

  /**
    * Transforms the notification form into a notification model
    * @return the [[Notification notification model]]
    */
  def toModel = Notification(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    `type` = `type`,
    title = title,
    text = text,
    owner = owner,
    eventTime = eventTime ?? new Date(),
    readTime = readTime ?? new Date(),
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Notification Form Companion
  * @author lawrence.daniels@gmail.com
  */
object NotificationForm {

  /**
    * Notification Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class NotificationFormExtensions(val model: Notification) extends AnyVal {

    /**
      * Transforms the notification model into a notification form
      * @return the [[NotificationForm notification form]]
      */
    def toForm = NotificationForm(
      _id = model._id.map(_.stringify),
      `type` = model.`type`,
      title = model.title,
      text = model.text,
      owner = model.owner,
      eventTime = model.eventTime,
      readTime = model.readTime,
      creationTime = model.creationTime
    )

  }

  implicit val NotificationFormat = Json.format[NotificationForm]

}