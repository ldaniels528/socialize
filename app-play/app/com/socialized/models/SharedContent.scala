package com.socialized.models

import java.util.Date

import play.api.libs.json.Json
import reactivemongo.bson.Macros

/**
  * Represents Shared Content; usually posted from a news site, etc.
  * @author lawrence.daniels@gmail.com
  */
case class SharedContent(author: Option[String] = None,
                         description: Option[String] = None,
                         locale: Option[String] = None,
                         publishedTime: Option[Date] = None,
                         section: Option[String] = None,
                         source: Option[String] = None,
                         tags: Option[List[String]] = None,
                         thumbnailUrl: Option[String],
                         title: Option[String] = None,
                         updatedTime: Option[Date] = None,
                         url: Option[String] = None) {

  def completeness = {
    val values = Seq(author, description, locale, publishedTime, section, source, tags, thumbnailUrl, title, updatedTime, url)
    val count = values.foldLeft[Int](0) { (total, value) => total + value.map(_ => 1).getOrElse(0) }
    count / values.length.toDouble
  }

}

/**
  * Shared Content Companion
  * @author lawrence.daniels@gmail.com
  */
object SharedContent {

  implicit val WebPageSummaryFormat = Json.format[SharedContent]

  implicit val WebPageSummaryHandler = Macros.handler[SharedContent]

}