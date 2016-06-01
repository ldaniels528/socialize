package com.socialized.dao.mongodb

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument => BS, _}

import scala.concurrent.ExecutionContext

/**
  * Likeable Entity DAO
  * @author lawrence.daniels@gmail.com
  */
trait LikeableEntityDAO[T] {

  def reactiveMongoApi: ReactiveMongoApi

  def collectionName: String

  private lazy val mc = reactiveMongoApi.db.collection[BSONCollection](collectionName)

  /**
    * Records the fact that a user "likes" a given entity by ID
    * @param entityID the given entity ID
    * @param userID the given user ID
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return the promise of an option of the updated entity
    */
  def like(entityID: String, userID: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    likeSpecial(entityID, userID, collectionName = "likedBy", collectionQtyName = "likes")
  }

  /**
    * Records the fact that a user "unlikes" a given entity by ID
    * @param entityID the given entity ID
    * @param userID the given user ID
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return the promise of an option of the updated entity
    */
  def unlike(entityID: String, userID: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    unlikeSpecial(entityID, userID, collectionName = "likedBy", collectionQtyName = "likes")
  }

  /**
    * Records the fact that a user "likes" a given entity by ID
    * @param entityID the given entity ID
    * @param userID the given user ID
    * @param collectionName the name of the custom collection
    * @param collectionQtyName the name of the collection quantity                    
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return the promise of an option of the updated entity
    */
  def likeSpecial(entityID: String, userID: String, collectionName: String, collectionQtyName: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.findAndUpdate(
      selector = BS(
        "_id" -> BSONObjectID(entityID),
        "$or" -> BSONArray(
          BS(collectionName -> BS("$nin" -> BSONArray(BSONString(userID)))),
          BS(collectionName -> BS("$exists" -> false))
        )),
      update = BS(
        "$addToSet" -> BS(collectionName -> userID),
        "$inc" -> BS(collectionQtyName -> BSONInteger(1))
      ),
      fetchNewObject = true
    ) map (_.result[T])
  }

  def unlikeSpecial(entityID: String, userID: String, collectionName: String, collectionQtyName: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.findAndUpdate(
      selector = BS("_id" -> BSONObjectID(entityID), collectionName -> BS("$in" -> BSONArray(BSONString(userID)))),
      update = BS(
        "$pull" -> BS(collectionName -> userID),
        "$inc" -> BS(collectionQtyName -> BSONInteger(-1))
      ),
      fetchNewObject = true,
      upsert = false) map (_.result[T])
  }

}
