package com.socialized.javascript

import io.scalajs.npm.express.Response
import io.scalajs.npm.mongodb.{FindAndUpdateOptions, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js

/**
  * Routes Package Object
  * @author lawrence.daniels@gmail.com
  */
package object routes {

  type NextFunction = js.Function0[Unit]

  /**
    * Data Access Object Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class DAOExtensions(val coll: Collection) extends AnyVal {

    @inline
    def findById[T <: js.Any](id: String)(implicit ec: ExecutionContext): Future[Option[T]] = {
      coll.findOneFuture[T]("_id" $eq id.$oid)
    }

    @inline
    def findById[T <: js.Any](id: String, fields: js.Array[String])(implicit ec: ExecutionContext): Future[Option[T]] = {
      coll.findOneFuture[T]("_id" $eq id.$oid, fields)
    }

    @inline
    def follow(entityID: String, userID: String): Future[FindAndModifyWriteOpResult] = {
      link(entityID, userID, entitySetName = "followers", entityQtyName = "totalFollowers")
    }

    @inline
    def unfollow(entityID: String, userID: String): Future[FindAndModifyWriteOpResult] = {
      unlink(entityID, userID, entitySetName = "followers", entityQtyName = "totalFollowers")
    }

    @inline
    def like(entityID: String, userID: String): Future[FindAndModifyWriteOpResult] = {
      link(entityID, userID, entitySetName = "likedBy", entityQtyName = "likes")
    }

    @inline
    def unlike(entityID: String, userID: String): Future[FindAndModifyWriteOpResult] = {
      unlink(entityID, userID, entitySetName = "likedBy", entityQtyName = "likes")
    }

    private def link(entityID: String, userID: String, entitySetName: String, entityQtyName: String) = {
      coll.findOneAndUpdate(
        filter = doc("_id" $eq new ObjectID(entityID), $or(entitySetName $nin js.Array(userID), entitySetName $exists false)),
        update = doc(entitySetName $addToSet userID, entityQtyName $inc 1),
        options = new FindAndUpdateOptions(upsert = false, returnOriginal = false)
      ).toFuture
    }

    private def unlink(entityID: String, userID: String, entitySetName: String, entityQtyName: String) = {
      coll.findOneAndUpdate(
        filter = doc("_id" $eq new ObjectID(entityID), entitySetName $in js.Array(userID)),
        update = doc(entitySetName $pull userID, entityQtyName $inc -1),
        options = new FindAndUpdateOptions(upsert = false, returnOriginal = false)
      ).toFuture
    }
  }

  /**
    * Parameter Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class ParameterExtensions(val params: js.Dictionary[String]) extends AnyVal {

    @inline
    def extractParams(names: String*): Option[Seq[String]] = {
      val values = names.map(params.get)
      if (values.forall(_.isDefined)) Some(values.flatten) else None
    }
  }

  /**
    * Response Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class ResponseExtensions(val response: Response) extends AnyVal {

    @inline
    def missingParams(params: String*): Unit = {
      val message = s"Bad Request: ${params.mkString(" and ")} ${if (params.length == 1) "is" else "are"} required"
      response.status(400).send(message)
    }
  }

}
