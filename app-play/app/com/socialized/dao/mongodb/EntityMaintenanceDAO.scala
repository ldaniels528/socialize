package com.socialized.dao.mongodb

import com.socialized.dao.DAOException
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument => BS, _}

import scala.concurrent.ExecutionContext

/**
  * Represents an Abstract DAO, which implements the basic CRUD methods for a entity of type "T"
  * @author lawrence.daniels@gmail.com
  */
trait EntityMaintenanceDAO[T] {

  def reactiveMongoApi: ReactiveMongoApi

  def collectionName: String

  private lazy val mc = reactiveMongoApi.db.collection[BSONCollection](collectionName)

  /**
    * Creates (persists) a new entity or item
    * @param item the given entity or item
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param writer the implicit [[BSONSerializationPack.Writer writer]]
    * @return a promise of the newly created entity or item
    */
  def create(item: T)(implicit ec: ExecutionContext, writer: BSONSerializationPack.Writer[T]) = {
    mc.insert(item) map {
      case result if result.inError => throw DAOException(result.message)
      case result => item
    }
  }

  /**
    * Deletes an entity or item by its unique identifier
    * @param id the entity or item's unique identifier
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of the [[reactivemongo.api.commands.WriteResult write result]]
    */
  def deleteOne(id: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.remove(query = BS("_id" -> BSONObjectID(id))) map {
      case result if result.inError => throw DAOException(result.message)
      case result => result
    }
  }

  /**
    * Retrieves a collection of all existing entities or items
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of a collection of all existing entities or items
    */
  def findAll(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.find(selector = BS()).cursor[T]().collect[List]()
  }

  /**
    * Attempts to retrieve an entity or item by its unique identifier
    * @param id the entity or item's unique identifier
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the requested entity or item
    */
  def findOne(id: String)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.find(selector = BS("_id" -> BSONObjectID(id))).one[T]
  }

  /**
    * Attempts to retrieve an entity or item by its unique identifier
    * @param id the entity or item's unique identifier
    * @param projection the given projection
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the requested entity or item
    */
  def findOne[P](id: String, projection: P)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T], pwriter: BSONSerializationPack.Writer[P]) = {
    mc.find(selector = BS("_id" -> BSONObjectID(id)), projection = projection).one[T]
  }

  /**
    * Attempts to retrieve an entity or item by its unique identifier
    * @param properties the collection of properties
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the requested entity or item
    */
  def findOneBy(properties: Producer[BSONElement]*)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.find(BS(properties: _*)).one[T]
  }

  /**
    * Retrieves user sessions by the given collection of user IDs
    * @param userIDs the given collection of user IDs
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return the collection of [[com.socialized.models.UserSession user sessions]]
    */
  def findByUsers(userIDs: Seq[String])(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.find(selector = BS("userID" -> BS("$in" -> BSONArray(userIDs.map(BSONObjectID(_)))))).cursor[T]().collect[List]()
  }

  /**
    * Retrieves a collection of all existing entities or items
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of a collection of all existing entities or items
    */
  def findMatches(properties: Producer[BSONElement]*)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    mc.find(BS(properties: _*)).cursor[T]().collect[List]()
  }

  /**
    * Updates the given entity or item by its unique identifier
    * @param id the entity or item's unique identifier
    * @param properties the given collection of properties to update
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param writer the implicit [[BSONSerializationPack.Writer writer]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the updated entity or item
    */
  def patch(id: Option[BSONObjectID], properties: Producer[BSONElement]*)(implicit ec: ExecutionContext, writer: BSONSerializationPack.Writer[T], reader: BSONSerializationPack.Reader[T]) = {
    mc.findAndUpdate(
      selector = BS("_id" -> id),
      update = BS(properties: _*),
      fetchNewObject = true,
      upsert = false) map (_.result[T])
  }

  /**
    * Updates the given entity or item via the given update properties
    * @param query the given [[Producer query elements]]
    * @param update the given collection of [[Producer properties]] to update
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param writer the implicit [[BSONSerializationPack.Writer writer]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the updated entity or item
    */
  def patchMatches(query: Producer[BSONElement]*)(update: Producer[BSONElement]*)(implicit ec: ExecutionContext, writer: BSONSerializationPack.Writer[T], reader: BSONSerializationPack.Reader[T]) = {
    mc.findAndUpdate(
      selector = BS(query: _*),
      update = BS(update: _*),
      fetchNewObject = true,
      upsert = false) map (_.result[T])
  }

  /**
    * Replaces the given entity or item by its unique identifier
    * @param id the entity or item's unique identifier
    * @param item the given entity or item instance
    * @param ec the implicit [[ExecutionContext execution context]]
    * @param writer the implicit [[BSONSerializationPack.Writer writer]]
    * @param reader the implicit [[BSONSerializationPack.Reader reader]]
    * @return a promise of an option of the updated entity or item
    */
  def replace(id: Option[BSONObjectID], item: T)(implicit ec: ExecutionContext, writer: BSONSerializationPack.Writer[T], reader: BSONSerializationPack.Reader[T]) = {
    mc.findAndUpdate(
      selector = BS("_id" -> id),
      update = item,
      fetchNewObject = true,
      upsert = false) map (_.result[T])
  }

}
