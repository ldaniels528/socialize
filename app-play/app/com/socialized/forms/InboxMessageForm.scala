package com.socialized.forms

import java.util.Date

import com.socialized.models.InboxMessage
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Inbox Message Form
  * @author lawrence.daniels@gmail.com
  */
case class InboxMessageForm(_id: Option[String],
                            title: Option[String],
                            message: Option[String],
                            creationTime: Option[Date]) {

  /**
    * Transforms the inboxMessage form into a inbox message model
    * @return the [[InboxMessage inbox message model]]
    */
  def toModel = InboxMessage(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    title = title,
    message = message,
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Inbox Message Form Companion
  * @author lawrence.daniels@gmail.com
  */
object InboxMessageForm {

  /**
    * Inbox Message Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class InboxMessageFormExtensions(val model: InboxMessage) extends AnyVal {

    /**
      * Transforms the inboxMessage model into a inboxMessage form
      * @return the [[InboxMessageForm inboxMessage form]]
      */
    def toForm = InboxMessageForm(
      _id = model._id.map(_.stringify) ?? BSONObjectID.generate.stringify,
      title = model.title,
      message = model.message,
      creationTime = model.creationTime ?? new Date()
    )

  }


  implicit val InboxMessageFormat = Json.format[InboxMessageForm]

}