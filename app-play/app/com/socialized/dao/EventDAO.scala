package com.socialized.dao

import com.socialized.dao.mongodb.EventDAOMongoDB
import com.socialized.models.{EntitySearchResult, Event}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Represents an Event DAO
  * @author lawrence.daniels@gmail.com
  */
trait EventDAO {

  /**
    * Retrieves events by its submitter
    * @param ownerID the given submitter ID
    * @param ec      the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[Event events]]
    */
  def findEventsByOwner(ownerID: String)(implicit ec: ExecutionContext): Future[List[Event]]

  /**
    * Performs a search for events matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  def search(searchTerm: String)(implicit ec: ExecutionContext): Future[List[EntitySearchResult]]

}

/**
  * Event DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object EventDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new EventDAOMongoDB(reactiveMongoApi)
  }

}