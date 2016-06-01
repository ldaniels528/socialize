package com.socialized.models

import java.util.Date

import com.socialized.models.AttachmentDispositions.AttachmentDisposition
import com.socialized.util.OptionHelper._
import play.api.libs.json._
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson._

/**
 * Represents a File Attachment
 * @author lawrence.daniels@gmail.com
 */
case class Attachment(_id: Option[BSONObjectID] = BSONObjectID.generate,
                      fileName: Option[String],
                      contentType: Option[String],
                      disposition: Option[AttachmentDisposition],
                      uploadedDate: Option[Date],
                      width: Option[Double] = None,
                      height: Option[Double] = None)

/**
 * Attachment Singleton
 * @author lawrence.daniels@gmail.com
 */
object Attachment {

  implicit val AttachmentFormat = Json.format[Attachment]

  implicit val AttachmentHandler = Macros.handler[Attachment]

}

