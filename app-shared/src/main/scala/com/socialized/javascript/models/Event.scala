package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents an event model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Event extends js.Object {
  var _id: js.UndefOr[String] = _
  var `type`: js.UndefOr[String] = _
  var permissions: js.UndefOr[String] = _
  var title: js.UndefOr[String] = _
  var avatarId: js.UndefOr[String] = _
  var avatarURL: js.UndefOr[String] = _
  var owner: js.UndefOr[Submitter] = _
  var eventTime: js.UndefOr[js.Date] = _
  var creationTime: js.UndefOr[js.Date] = _
}
