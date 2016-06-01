package com.socialized.models

import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}
import reactivemongo.bson.{BSONHandler, BSONString}

/**
 * Represents an enumeration of attachment dispositions
 * @author lawrence.daniels@gmail.com
 */
object AttachmentDispositions extends Enumeration {
  type AttachmentDisposition = Value

  val PHOTO = Value("PHOTO")
  val THUMBNAIL = Value("THUMBNAIL")
  val OTHER = Value("OTHER")

  /**
   * Attachment Disposition Format
   * @author lawrence.daniels@gmail.com
   */
  implicit object AttachmentDispositionFormat extends Format[AttachmentDisposition] {

    def reads(json: JsValue) = JsSuccess(AttachmentDispositions.withName(json.as[String]))

    def writes(contestStatus: AttachmentDisposition) = JsString(contestStatus.toString)
  }

  /**
   * Attachment Disposition Handler
   * @author lawrence.daniels@gmail.com
   */
  implicit object AttachmentDispositionHandler extends BSONHandler[BSONString, AttachmentDisposition] {

    def read(string: BSONString) = AttachmentDispositions.withName(string.value)

    def write(disposition: AttachmentDisposition) = BSONString(disposition.toString)
  }

}
