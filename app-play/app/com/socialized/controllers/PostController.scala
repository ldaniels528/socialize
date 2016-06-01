package com.socialized.controllers

import java.util.Date
import javax.inject.Inject

import com.socialized.actors.WebSockets
import com.socialized.dao.{PostDAO, SharedContentParser, UserDAO}
import com.socialized.forms.AttachmentForm._
import com.socialized.forms.PostForm
import com.socialized.forms.PostForm._
import com.socialized.models.{Comment, OperationResult, Reply}
import com.socialized.util.OptionHelper._
import play.api.Logger
import play.api.libs.Files
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, MultipartFormData}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.mvc.Http
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.bson.{BSONObjectID, BSONDocument => BS, _}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Posts Controller
  * @author lawrence.daniels@gmail.com
  */
class PostController @Inject()(val reactiveMongoApi: ReactiveMongoApi) extends Controller
  with MongoController with ReactiveMongoComponents with AttachmentsCapability with SharedContentParser {

  val gridFS = GridFS[BSONSerializationPack.type](db, "post_attachments")
  val postDAO = PostDAO(reactiveMongoApi)
  val userDAO = UserDAO(reactiveMongoApi)

  ///////////////////////////////////////////////////////////////////////////
  //      Post API
  ///////////////////////////////////////////////////////////////////////////

  def createPost = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[PostForm]) match {
      case Some(form) =>
        postDAO.create(form.toModel) map { newPost =>
          WebSockets ! newPost
          Ok(Json.toJson(newPost.toForm()))
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Post expected"))
    }
  }

  def deletePostByID(postID: String) = Action.async {
    postDAO.deleteOne(postID) map {
      case result => Ok(Json.toJson(OperationResult(success = result.n > 0, message = result.errmsg)))
    } recover { case e =>
      Logger.error(s"Failed to delete post '$postID'", e)
      InternalServerError(s"Failed to delete post '$postID'")
    }
  }

  def getNewsFeed(userID: String) = Action.async {
    val outcome = for {
      userOpt <- userDAO.findOne(userID)
      submitterIds = userID :: (userOpt match {
        case Some(user) => user.followers.getOrElse(Nil)
        case None => Nil
      })
      posts <- postDAO.findPostsBySubmitters(submitterIds)
    } yield posts

    outcome map { posts =>
      Ok(Json.toJson(posts.map(_.toForm())))
    } recover { case e =>
      Logger.error(s"Failed to retrieve posts for user '$userID'", e)
      InternalServerError(e.getMessage)
    }
  }

  def getSharedContent(url: String) = Action { implicit request =>
    Try(parseMetaData(url)) match {
      case Success(Some((summary))) => Ok(Json.toJson(summary))
      case Success(None) => BadRequest("URL expected")
      case Failure(e) => InternalServerError(e.getMessage)
    }
  }

  def getPostByID(postID: String) = Action.async {
    postDAO.findOne(postID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(postID)
    } recover { case e =>
      Logger.error(s"Failed to retrieve post '$postID'", e)
      InternalServerError(s"Failed to retrieve post '$postID'")
    }
  }

  def getPosts = Action.async {
    postDAO.findAll map { posts =>
      Ok(Json.toJson(posts.map(_.toForm())))
    } recover { case e =>
      Logger.error(s"Failed to retrieve posts (user unspecified)", e)
      InternalServerError(e.getMessage)
    }
  }

  def getPostsByUserID(userID: String) = Action.async {
    postDAO.findPostsByUserID(userID) map { posts =>
      Ok(Json.toJson(posts.map(_.toForm())))
    } recover { case e =>
      Logger.error(s"Failed to retrieve posts for user '$userID'", e)
      InternalServerError(e.getMessage)
    }
  }

  def getPostsByTags(tags: List[String]) = Action.async {
    postDAO.findPostsByTags(tags) map { posts =>
      Ok(Json.toJson(posts.map(_.toForm())))
    } recover { case e =>
      Logger.error(s"Failed to retrieve posts for tags '${tags.mkString(", ")}'", e)
      InternalServerError(e.getMessage)
    }
  }

  def likePost(postID: String, userID: String) = Action.async {
    postDAO.likePost(postID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(postID)
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  def unlikePost(postID: String, userID: String) = Action.async {
    postDAO.unlikePost(postID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(postID)
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Comments API
  ///////////////////////////////////////////////////////////////////////////

  def createComment(postID: String) = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[Comment]) match {
      case Some(comment) =>
        val newComment = comment.copy(_id = BSONObjectID.generate.stringify, creationTime = new Date())
        postDAO.addComment(postID, newComment) map {
          case Some(post) => Ok(Json.toJson(post.toForm()))
          case None => NotFound(postID)
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Comment expected"))
    }
  }

  def likeComment(postID: String, commentID: String, userID: String) = Action.async {
    postDAO.likeComment(postID, commentID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(postID)
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  def unlikeComment(postID: String, commentID: String, userID: String) = Action.async {
    postDAO.unlikeComment(postID, commentID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(postID)
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Reply API
  ///////////////////////////////////////////////////////////////////////////

  def createReply(postID: String, commentID: String) = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[Reply]) match {
      case Some(reply) =>
        val newReply = reply.copy(_id = BSONObjectID.generate, creationTime = new Date())
        postDAO.addReply(postID, commentID, newReply) map {
          case Some(post) => Ok(Json.toJson(post.toForm()))
          case None => NotFound(postID)
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Comment expected"))
    }
  }

  def likeReply(postID: String, commentID: String, replyID: String, userID: String) = Action.async {
    postDAO.likeReply(postID, commentID, replyID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(s"Comment $replyID")
    } recover {
      case e =>
        e.printStackTrace()
        InternalServerError(e.getMessage)
    }
  }

  def unlikeReply(postID: String, commentID: String, replyID: String, userID: String) = Action.async {
    postDAO.unlikeReply(postID, commentID, replyID, userID) map {
      case Some(post) => Ok(Json.toJson(post.toForm()))
      case None => NotFound(s"Comment $replyID")
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Attachments API
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Downloads the content for the given attachment ID
    * @param id the given attachment ID
    * @example GET http://localhost:9000/api/attachment/563cff871b591f697970aaa3
    */
  def downloadAttachment(id: String) = Action.async {
    gridFS.find(BS("_id" -> BSONObjectID(id))).headOption map {
      case None => NotFound(id)
      case Some(file) =>
        val filename = file.filename.getOrElse("unnamed")
        Ok.feed(gridFS.enumerate(file))
          .as(file.contentType.getOrElse(Http.MimeTypes.BINARY))
          .withHeaders(
            CONTENT_LENGTH -> file.length.toString,
            CONTENT_DISPOSITION -> s"""$CONTENT_DISPOSITION_INLINE; filename="$filename""""
          )
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  /**
    * Retrieves all attachment identifiers for the given post
    * @param postID the given post ID
    * @example GET http://localhost:9000/api/post/563cff811b591f4c7870aaa1/attachments
    */
  def getAttachmentsByPostID(postID: String) = Action.async {
    postDAO.findAttachmentIDsByPostID(postID) map { ids =>
      Ok(Json.toJson(ids))
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  /**
    * Retrieves all attachment identifiers for the given post
    * @param userID the given user ID
    * @example GET http://localhost:9000/api/posts/attachments/user/5633c756d9d5baa77a714803
    */
  def getAttachmentsByUserID(userID: String) = Action.async {
    postDAO.findAttachmentsByUserID(userID) map { attachments =>
      Ok(Json.toJson(attachments.map(_.toForm)))
    } recover {
      case e =>
        InternalServerError(e.getMessage)
    }
  }

  def updatePost = Action.async { implicit request =>
    request.body.asJson.flatMap(_.asOpt[PostForm]) match {
      case Some(form) =>
        postDAO.update(form.toModel) map {
          case Some(updatedPost) =>
            WebSockets ! updatedPost
            Ok(Json.toJson(updatedPost.toForm()))
          case None => NotFound(form._id.orNull)
        } recover { case e =>
          InternalServerError(e.getMessage)
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Post expected"))
    }
  }

  /**
    * Uploads an attachment for the given post and user
    * @param postID the given post ID
    * @param userID the given user ID
    * @example POST http://localhost:9000/api/post/563cff811b591f4c7870aaa1/attachment/5633c756d9d5baa77a714803
    */
  def uploadAttachment(postID: String, userID: String) = Action.async { implicit request =>
    Logger.info("postID = %s, userID = %s".format(postID, userID))
    request.body.asMultipartFormData match {
      case Some(form: MultipartFormData[Files.TemporaryFile]) =>
        val metadata = BS("userID" -> BSONObjectID(userID), "postID" -> BSONObjectID(postID))
        val outcome = for {
          attachments <- Future.sequence(form.files map (saveAttachment(_, metadata)))
          updatedPost <- postDAO.addAttachment(postID, attachments)
        } yield updatedPost

        outcome map {
          case Some(updatedPost) => Ok(Json.toJson(updatedPost.toForm()))
          case None => NotFound(postID)
        } recover {
          case e =>
            InternalServerError(e.getMessage)
        }
      case None =>
        Future.successful(BadRequest("Multipart form expected"))
    }
  }

}
