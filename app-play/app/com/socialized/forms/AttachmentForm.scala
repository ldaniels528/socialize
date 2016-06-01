package com.socialized.forms

import java.util.Date

import com.socialized.models.Attachment
import com.socialized.models.AttachmentDispositions
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Attachment Form
  * @author lawrence.daniels@gmail.com
  */
case class AttachmentForm(_id: Option[String],
                          fileName: Option[String],
                          contentType: Option[String],
                          disposition: Option[String],
                          uploadedDate: Option[Date],
                          width: Option[Double],
                          height: Option[Double]) {

  /**
    * Transforms the post form into a post model
    * @return the [[Attachment attachment]]
    */
  def toModel = Attachment(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    fileName = fileName,
    contentType = contentType,
    disposition = disposition.map(AttachmentDispositions.withName),
    uploadedDate = uploadedDate ?? new Date(),
    width = width,
    height = height
  )

}

/**
  * Attachment Form Companion
  * @author lawrence.daniels@gmail.com
  */
object AttachmentForm {

  /**
    * Attachment Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class AttachmentFormExtensions(val model: Attachment) extends AnyVal {

    def toForm = AttachmentForm(
      _id = model._id.map(_.stringify),
      fileName = model.fileName,
      contentType = model.contentType,
      disposition = model.disposition.map(_.toString),
      uploadedDate = model.uploadedDate ?? new Date(),
      width = model.width,
      height = model.height
    )

  }

  implicit val AttachmentFormFormat = Json.format[AttachmentForm]

}