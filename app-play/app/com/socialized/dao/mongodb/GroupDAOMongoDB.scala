package com.socialized.dao.mongodb

import com.socialized.util.OptionHelper._
import com.socialized.dao.GroupDAO
import com.socialized.models.{EntitySearchResult, Group}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

/**
  * Group DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class GroupDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends GroupDAO
  with EntityMaintenanceDAO[Group] with EntitySearchDAO[Group] with LikeableEntityDAO[Group] {

  override val collectionName = "groups"

  /**
    * Performs a search for groups matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  override def search(searchTerm: String)(implicit ec: ExecutionContext) = {
    doSearch(searchTerm, "name") { g =>
      EntitySearchResult(_id = g._id, name = g.name, description = "Group", avatarURL = g.avatarURL, `type` = "GROUP")
    }
  }

}
