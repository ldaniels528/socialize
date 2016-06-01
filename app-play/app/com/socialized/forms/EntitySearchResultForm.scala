package com.socialized.forms

import java.util.Date

import com.socialized.models.EntitySearchResult
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Entity Search Result Form 
  * @author lawrence.daniels@gmail.com
  */
case class EntitySearchResultForm(_id: Option[String],
                                  name: Option[String],
                                  description: Option[String],
                                  `type`: Option[String],
                                  avatarURL: Option[String],
                                  creationTime: Option[Date]) {

  /**
    * Transforms the entity search result form into an entity search result model
    * @return the [[EntitySearchResult entity search result model]]
    */
  def toModel = EntitySearchResult(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    name = name,
    description = description,
    `type` = `type`,
    avatarURL = avatarURL,
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Entity Search Result Companion 
  * @author lawrence.daniels@gmail.com
  */
object EntitySearchResultForm {

  /**
    * EntitySearchResult Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class EntitySearchResultFormExtensions(val model: EntitySearchResult) extends AnyVal {

    /**
      * Transforms the entity search result model into an entity search result form
      * @return the [[EntitySearchResultForm entity search result form]]
      */
    def toForm = EntitySearchResultForm(
      _id = model._id.map(_.stringify),
      name = model.name,
      description = model.description,
      `type` = model.`type`,
      avatarURL = model.avatarURL,
      creationTime = model.creationTime
    )

  }

  implicit val EntitySearchResultFormat = Json.format[EntitySearchResultForm]

}