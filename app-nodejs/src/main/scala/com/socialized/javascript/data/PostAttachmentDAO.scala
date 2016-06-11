package com.socialized.javascript.data

import com.github.ldaniels528.meansjs.nodejs.mongodb.gridfs.{GridFSBucket, GridFSOptions}
import com.github.ldaniels528.meansjs.nodejs.mongodb.{Db, MongoDB}

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

    def getPostAttachmentDAO(implicit ec: ExecutionContext, mongo: MongoDB) = {
      mongo.GridFSBucket(db, new GridFSOptions(bucketName = "post_attachments")).asInstanceOf[PostAttachmentDAO]
    }

  }

}