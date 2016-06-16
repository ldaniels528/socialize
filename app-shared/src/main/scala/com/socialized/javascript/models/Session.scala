package com.socialized.javascript.models

import org.scalajs.nodejs.util.ScalaJsHelper._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a user session model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Session extends js.Object {
  var _id: js.UndefOr[String] = _
  var userID: js.UndefOr[String] = _
  var screenName: js.UndefOr[String] = _
  var primaryEmail: js.UndefOr[String] = _
  var avatarURL: js.UndefOr[String] = _
  var isAnonymous: js.UndefOr[Boolean] = _
  var creationTime: js.UndefOr[js.Date] = _
  var lastUpdated: js.UndefOr[Double] = _
}

/**
  * Session Companion
  * @author lawrence.daniels@gmail.com
  */
object Session {

  def apply(_id: js.UndefOr[String] = js.undefined,
            userID: js.UndefOr[String] = js.undefined,
            screenName: js.UndefOr[String] = js.undefined,
            primaryEmail: js.UndefOr[String] = js.undefined,
            avatarURL: js.UndefOr[String] = js.undefined,
            isAnonymous: js.UndefOr[Boolean] = js.undefined,
            creationTime: js.UndefOr[js.Date] = js.undefined,
            lastUpdated: js.UndefOr[Double] = js.undefined) = {
    val session = new Session()
    session._id = _id ?? session._id
    session.userID = userID ?? session.userID
    session.screenName = screenName ?? session.screenName
    session.primaryEmail = primaryEmail ?? session.primaryEmail
    session.avatarURL = avatarURL ?? session.avatarURL
    session.isAnonymous = isAnonymous ?? session.isAnonymous
    session.creationTime = creationTime ?? session.creationTime
    session.lastUpdated = lastUpdated ?? session.lastUpdated
    session
  }

}