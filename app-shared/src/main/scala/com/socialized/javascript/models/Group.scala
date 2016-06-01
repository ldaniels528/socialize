package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a group model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Group extends js.Object {
  var _id: js.UndefOr[String] = _
  var name: js.UndefOr[String] = _
  var avatarId: js.UndefOr[String] = _
  var avatarURL: js.UndefOr[String] = _
  var creationTime: js.UndefOr[js.Date] = _

}

/**
  * Group Companion
  * @author lawrence.daniels@gmail.com
  */
object Group {

  def apply(_id: js.UndefOr[String] = js.undefined,
            name: js.UndefOr[String] = js.undefined,
            avatarId: js.UndefOr[String] = js.undefined,
            avatarURL: js.UndefOr[String] = js.undefined,
            creationTime: js.UndefOr[js.Date] = js.undefined) = {
    val group = new Group()
    group._id = _id
    group.name = name
    group.avatarId = avatarId
    group.avatarURL = avatarURL
    group.creationTime = creationTime
    group
  }

}