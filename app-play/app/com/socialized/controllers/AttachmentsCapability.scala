package com.socialized.controllers

import java.io.File
import javax.imageio.ImageIO

import com.socialized.controllers.AttachmentsCapability._
import com.socialized.models.AttachmentDispositions._
import com.socialized.util.OptionHelper._
import org.imgscalr.Scalr
import play.api.Logger
import play.api.libs.Files.TemporaryFile
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.mvc.Controller
import play.api.mvc.MultipartFormData.FilePart
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.api.gridfs.{DefaultFileToSave, GridFS}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.util.{Failure, Success, Try}

/**
  * Attachments Capability
  * @author lawrence.daniels@gmail.com
  */
trait AttachmentsCapability {
  self: Controller =>

  def gridFS: GridFS[BSONSerializationPack.type]

  /**
    * Persists the given file part to the database
    * @param filePart the given [[FilePart]]
    * @return the [[BSONObjectID]] of the saved attachment
    */
  def saveAttachment(filePart: FilePart[TemporaryFile], metadata: BSONDocument = BSONDocument()) = {
    // gets an enumerator from this file
    val enumerator = Enumerator.fromFile(filePart.ref.file)

    // if the attachment is an image, retrieve it's dimensions
    val image_? = determineDisposition(filePart.contentType) match {
      case PHOTO => Option(ImageIO.read(filePart.ref.file))
      case _ => None
    }

    // create the file metadata
    val fileToSave = DefaultFileToSave(
      filename = Some(filePart.filename),
      contentType = filePart.contentType,
      uploadDate = System.currentTimeMillis(),
      metadata = metadata)

    // gets a promise of the file fed by the enumerator above
    val promisedFile = gridFS.save(enumerator, file = fileToSave)

    // map the value of this future to the id
    promisedFile.map(
      readFile => AttachmentInfo(readFile.id.asInstanceOf[BSONObjectID],
        width = image_?.map(_.getWidth.toDouble),
        height = image_?.map(_.getHeight.toDouble))
    )
  }

  /**
    * Persists the given file part to the database
    * @param filePart the given [[FilePart]]
    * @return the [[BSONObjectID]] of the saved attachment
    */
  def saveThumbNail(filePart: FilePart[TemporaryFile]) = {
    Try {
      // scale the original image
      val thumbnailFile = File.createTempFile(filePart.filename, "img")
      Option(ImageIO.read(filePart.ref.file)) map { image =>
        val aspectRatio = image.getWidth.toDouble / image.getHeight.toDouble
        val scaledWidth = 300
        val scaledHeight = (scaledWidth * aspectRatio).toInt
        val thumbnail = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, scaledWidth, scaledHeight, Scalr.OP_ANTIALIAS)

        // write the thumbnail to disk
        ImageIO.write(thumbnail, extractFormatName(filePart.contentType), thumbnailFile)

        // gets an enumerator from this file
        val enumerator = Enumerator.fromFile(thumbnailFile)

        // create the file metadata
        val fileToSave = DefaultFileToSave(
          filename = Some(filePart.filename),
          contentType = filePart.contentType,
          uploadDate = System.currentTimeMillis())

        // gets a promise of the file fed by the enumerator above
        val promisedFile = gridFS.save(enumerator, file = fileToSave)

        // map the value of this future to the id
        promisedFile.map { readFile =>
          thumbnailFile.delete() match {
            case true => Logger.info(s"Successfully deleted temporary file ${thumbnailFile.getName}")
            case false => Logger.warn(s"Failed to delete temporary file ${thumbnailFile.getAbsolutePath}")
          }
          AttachmentInfo(readFile.id.asInstanceOf[BSONObjectID], scaledWidth.toDouble, scaledHeight.toDouble)
        }
      }
    } match {
      case Success(outcome) => outcome
      case Failure(e) =>
        Logger.error(s"Failed to create thumbnail of image file ${filePart.filename}", e)
        None
    }
  }

  private def determineDisposition(contentType: Option[String]) = {
    contentType match {
      case Some(s) if s.contains("image") => PHOTO
      case _ => OTHER
    }
  }

  private def extractFormatName(contentType: Option[String]) = {
    contentType.map { ct =>
      ct.indexOf("/") match {
        case -1 => "jpeg"
        case index => ct.substring(index + 1)
      }
    } getOrElse "jpeg"
  }

}

/**
  * Attachments Controller Singleton
  * @author lawrence.daniels@gmail.com
  */
object AttachmentsCapability {

  case class AttachmentInfo(_id: BSONObjectID, width: Option[Double] = None, height: Option[Double] = None)

}