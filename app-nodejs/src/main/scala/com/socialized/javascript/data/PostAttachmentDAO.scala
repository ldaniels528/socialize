package com.socialized.javascript.data

import io.scalajs.npm.mongodb.Db
import io.scalajs.npm.mongodb.gridfs.{GridFSBucket, GridFSOptions}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Post Attachment DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait PostAttachmentDAO extends GridFSBucket

/**
  * Post Attachment DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object PostAttachmentDAO {

  /**
    * Post Attachment DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class PostAttachmentDAOExtensions(val db: Db) extends AnyVal {

    def getPostAttachmentDAO(implicit ec: ExecutionContext): PostAttachmentDAO = {
      new GridFSBucket(db, new GridFSOptions(bucketName = "post_attachments")).asInstanceOf[PostAttachmentDAO]
    }

  }

}