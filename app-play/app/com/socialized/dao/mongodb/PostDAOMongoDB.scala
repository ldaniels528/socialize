package com.socialized.dao.mongodb

import java.util.Date

import com.socialized.controllers.AttachmentsCapability.AttachmentInfo
import com.socialized.dao.PostDAO
import com.socialized.models._
import com.socialized.util.OptionHelper._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.bson.{BSONArray, BSONInteger, BSONObjectID, BSONString, BSONDocument => BS, _}

import scala.concurrent.ExecutionContext

/**
  * Post DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class PostDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends PostDAO
  with EntityMaintenanceDAO[Post] with LikeableEntityDAO[Post] {

  lazy val gridFS = GridFS[BSONSerializationPack.type](reactiveMongoApi.db, "post_attachments")
  lazy val mcPosts = reactiveMongoApi.db.collection[BSONCollection](collectionName)

  override val collectionName = "posts"

  ///////////////////////////////////////////////////////////////////////////
  //      Post Methods
  ///////////////////////////////////////////////////////////////////////////

  override def findPostsByUserID(userID: String)(implicit ec: ExecutionContext) = {
    findMatches("submitterId" -> userID)
  }

  override def findPostsBySubmitters(submitters: Seq[String])(implicit ec: ExecutionContext) = {
    findMatches("submitterId" -> BS("$in" -> BSONArray(submitters.map(BSONString))))
  }

  override def findPostsByTags(tags: List[String])(implicit ec: ExecutionContext) = {
    if (tags.isEmpty) findAll else findMatches("tags" -> BS("$in" -> BSONArray(tags.map(BSONString))))
  }

  override def likePost(postID: String, userID: String)(implicit ec: ExecutionContext) = {
    like(postID, userID)
  }

  override def unlikePost(postID: String, userID: String)(implicit ec: ExecutionContext) = {
    unlike(postID, userID)
  }

  override def update(post: Post)(implicit ec: ExecutionContext) = {
    replace(post._id, post)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Comment Methods
  ///////////////////////////////////////////////////////////////////////////

  override def addComment(postID: String, newComment: Comment)(implicit ec: ExecutionContext) = {
    patch(id = BSONObjectID(postID), "$addToSet" -> BS("comments" -> newComment))
  }

  override def likeComment(postID: String, commentID: String, userID: String)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "comments" -> BS("$elemMatch" -> BS("_id" -> BSONObjectID(commentID), "likedBy" -> BS("$nin" -> BSONArray(BSONString(userID)))))),
      update = BS(
        "$inc" -> BS("comments.$.likes" -> BSONInteger(1)),
        "$addToSet" -> BS("comments.$.likedBy" -> userID)
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  override def unlikeComment(postID: String, commentID: String, userID: String)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "comments" -> BS("$elemMatch" -> BS("_id" -> BSONObjectID(commentID), "likedBy" -> BS("$in" -> BSONArray(BSONString(userID)))))),
      update = BS(
        "$inc" -> BS("comments.$.likes" -> BSONInteger(-1)),
        "$pull" -> BS("comments.$.likedBy" -> userID)
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Reply Methods
  ///////////////////////////////////////////////////////////////////////////

  override def addReply(postID: String, commentID: String, newReply: Reply)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "comments._id" -> BSONObjectID(commentID)),
      update = BS(
        "$addToSet" -> BS("comments.$.replies" -> newReply),
        "$addToSet" -> BS("replyLikes" -> BS("_id" -> newReply._id, "likes" -> BSONInteger(0), "likedBy" -> BSONArray()))
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  override def deleteReply(postID: String, commentID: String, replyID: String)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "comments._id" -> BSONObjectID(commentID)),
      update = BS("$pull" -> BS("comments.$.replies" -> BS("_id" -> BSONObjectID(replyID)))),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  override def likeReply(postID: String, commentID: String, replyID: String, userID: String)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "replyLikes" -> BS("$elemMatch" -> BS("_id" -> BSONObjectID(replyID), "likedBy" -> BS("$nin" -> BSONArray(BSONString(userID)))))),
      update = BS(
        "$inc" -> BS("replyLikes.$.likes" -> BSONInteger(1)),
        "$addToSet" -> BS("replyLikes.$.likedBy" -> userID)
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  override def unlikeReply(postID: String, commentID: String, replyID: String, userID: String)(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID), "replyLikes" -> BS("$elemMatch" -> BS("_id" -> BSONObjectID(replyID), "likedBy" -> BS("$in" -> BSONArray(BSONString(userID)))))),
      update = BS(
        "$inc" -> BS("replyLikes.$.likes" -> BSONInteger(-1)),
        "$pull" -> BS("replyLikes.$.likedBy" -> userID)
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[Post])
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Attachment Methods
  ///////////////////////////////////////////////////////////////////////////

  override def addAttachment(postID: String, attachments: Seq[AttachmentInfo])(implicit ec: ExecutionContext) = {
    mcPosts.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(postID)),
      update = BS("$addToSet" -> BS("attachments" -> BS("$each" -> attachments.map(_._id.stringify)))),
      fetchNewObject = false, upsert = false) map (_.result[Post])
  }

  override def findAttachmentIDsByPostID(postID: String)(implicit ec: ExecutionContext) = {
    gridFS.find(BS("metadata.postID" -> BSONObjectID(postID))).collect[List]() map { files =>
      files.map(file => file.id.asInstanceOf[BSONObjectID].stringify)
    }
  }

  override def findAttachmentsByUserID(userID: String)(implicit ec: ExecutionContext) = {
    gridFS.find(BS("metadata.userID" -> BSONObjectID(userID))).collect[List]() map { files =>
      files.map { file =>
        Attachment(
          _id = file.id.asInstanceOf[BSONObjectID],
          fileName = file.filename,
          contentType = file.contentType,
          disposition = AttachmentDispositions.PHOTO,
          uploadedDate = file.uploadDate.map(new Date(_)),
          width = file.metadata.getAs[Double]("width"),
          height = file.metadata.getAs[Double]("height"))
      }
    }
  }

}
