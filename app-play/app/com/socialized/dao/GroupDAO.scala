package com.socialized.dao

import com.socialized.dao.mongodb.GroupDAOMongoDB
import com.socialized.models.EntitySearchResult
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Represents a Group DAO
  * @author lawrence.daniels@gmail.com
  */
trait GroupDAO {

  /**
    * Performs a search for groups matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  def search(searchTerm: String)(implicit ec: ExecutionContext): Future[List[EntitySearchResult]]

}

/**
  * Group DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object GroupDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new GroupDAOMongoDB(reactiveMongoApi)
  }

}