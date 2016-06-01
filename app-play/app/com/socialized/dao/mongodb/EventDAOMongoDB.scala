package com.socialized.dao.mongodb

import com.socialized.util.OptionHelper._
import com.socialized.dao.EventDAO
import com.socialized.models.{EntitySearchResult, Event}

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext

/**
  * Event DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class EventDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends EventDAO with EntityMaintenanceDAO[Event] with EntitySearchDAO[Event] {
  val collectionName = "events"

  /**
    * Retrieves events by its submitter
    * @param ownerID the given submitter ID
    * @param ec      the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[Event events]]
    */
  override def findEventsByOwner(ownerID: String)(implicit ec: ExecutionContext) = {
    findMatches("owner._id" -> BSONObjectID(ownerID))
  }

  /**
    * Performs a search for events matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  override def search(searchTerm: String)(implicit ec: ExecutionContext) = {
    doSearch(searchTerm, "title") { evt =>
      EntitySearchResult(_id = evt._id, name = evt.title, description = "Event", avatarURL = None, `type` = "EVENT")
    }
  }

}