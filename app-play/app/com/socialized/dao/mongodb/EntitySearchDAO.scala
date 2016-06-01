package com.socialized.dao.mongodb

import com.socialized.models.EntitySearchResult
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.BSONSerializationPack
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocument => BS}

import scala.concurrent.ExecutionContext

/**
  * Entity Search DAO
  * @author lawrence.daniels@gmail.com
  */
trait EntitySearchDAO[T] {

  def reactiveMongoApi: ReactiveMongoApi

  def collectionName: String

  private lazy val mc = reactiveMongoApi.db.collection[BSONCollection](collectionName)

  /**
    * Performs a search for entities or items matching the given search term
    * @param searchTerm the given search term
    * @param transform a function that transform a given entity or item into a [[EntitySearchResult search result]]
    * @param ec the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  def doSearch(searchTerm: String, fields: String*)(transform: T => EntitySearchResult)(implicit ec: ExecutionContext, reader: BSONSerializationPack.Reader[T]) = {
    val queries = fields.foldLeft[List[BS]](Nil) { (list, field) =>
      BS(field -> BS("$regex" -> s"^$searchTerm", "$options" -> "i")) :: list
    }
    val selector = queries match {
      case doc :: Nil => doc
      case docs => BS("$or" -> BSONArray(docs))
    }
    mc.find(selector).cursor[T]().collect[List]().map(_.map(transform))
  }

}
