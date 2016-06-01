package com.socialized.dao.mongodb

import com.socialized.util.OptionHelper._
import com.socialized.dao.OrganizationDAO
import com.socialized.models.{EntitySearchResult, Group}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

/**
  * Organization DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class OrganizationDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends OrganizationDAO
  with EntityMaintenanceDAO[Group] with EntitySearchDAO[Group] with LikeableEntityDAO[Group] {

  override val collectionName = "organizations"

  /**
    * Performs a search for groups matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  override def search(searchTerm: String)(implicit ec: ExecutionContext) = {
    doSearch(searchTerm, "name") { o =>
      EntitySearchResult(_id = o._id, name = o.name, description = "Organization", avatarURL = o.avatarURL, `type` = "ORGANIZATION")
    }
  }

}
