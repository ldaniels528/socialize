package com.socialized.dao

import com.socialized.controllers.AttachmentsCapability.AttachmentInfo
import com.socialized.dao.mongodb.PostDAOMongoDB
import com.socialized.models._
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Post DAO
  * @author lawrence.daniels@gmail.com
  */
trait PostDAO {

  ///////////////////////////////////////////////////////////////////////////
  //      Post Methods
  ///////////////////////////////////////////////////////////////////////////

  def findPostsByUserID(userID: String)(implicit ec: ExecutionContext): Future[List[Post]]

  def findPostsBySubmitters(submitters: Seq[String])(implicit ec: ExecutionContext): Future[List[Post]]

  def findPostsByTags(tags: List[String])(implicit ec: ExecutionContext): Future[List[Post]]

  def likePost(postID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  def unlikePost(postID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  def update(post: Post)(implicit ec: ExecutionContext): Future[Option[Post]]

  ///////////////////////////////////////////////////////////////////////////
  //      Comment Methods
  ///////////////////////////////////////////////////////////////////////////

  def addComment(postID: String, newComment: Comment)(implicit ec: ExecutionContext): Future[Option[Post]]

  def likeComment(postID: String, commentID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  def unlikeComment(postID: String, commentID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  ///////////////////////////////////////////////////////////////////////////
  //      Reply Methods
  ///////////////////////////////////////////////////////////////////////////

  def addReply(postID: String, commentID: String, newReply: Reply)(implicit ec: ExecutionContext): Future[Option[Post]]

  def deleteReply(postID: String, commentID: String, replyID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  def likeReply(postID: String, commentID: String, replyID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  def unlikeReply(postID: String, commentID: String, replyID: String, userID: String)(implicit ec: ExecutionContext): Future[Option[Post]]

  ///////////////////////////////////////////////////////////////////////////
  //      Attachment Methods
  ///////////////////////////////////////////////////////////////////////////

  def addAttachment(postID: String, attachments: Seq[AttachmentInfo])(implicit ec: ExecutionContext): Future[Option[Post]]

  def findAttachmentIDsByPostID(postID: String)(implicit ec: ExecutionContext): Future[List[String]]

  def findAttachmentsByUserID(userID: String)(implicit ec: ExecutionContext): Future[List[Attachment]]

}

/**
  * Post DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object PostDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new PostDAOMongoDB(reactiveMongoApi)
  }

}