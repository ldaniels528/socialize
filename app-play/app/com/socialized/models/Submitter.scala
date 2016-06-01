package com.socialized.models

import play.api.libs.json.Json
import reactivemongo.bson._

/**
  * Represents a Submitter; used for posts, etc.
  * @author lawrence.daniels@gmail.com
  */
case class Submitter(_id: Option[String], name: Option[String], avatarURL: Option[String])

/**
  * Submitter Companion
  * @author lawrence.daniels@gmail.com
  */
object Submitter {

  implicit val SubmitterFormat = Json.format[Submitter]

  implicit val SubmitterHandler = Macros.handler[Submitter]

}
