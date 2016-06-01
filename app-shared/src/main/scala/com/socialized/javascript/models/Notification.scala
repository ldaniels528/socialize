package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a notification model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Notification extends js.Object {
  var _id: js.UndefOr[String] = _
  var `type`: js.UndefOr[String] = _
  var title: js.UndefOr[String] = _
  var text: js.UndefOr[String] = _
  var owner: js.UndefOr[Submitter] = _
  var eventTime: js.UndefOr[js.Date] = _
  var readTime: js.UndefOr[js.Date] = _
  var creationTime: js.UndefOr[js.Date] = _
}

/**
  * Notification Companion Object
  * @author lawrence.daniels@gmail.com
  */
object Notification {
  val ACHIEVEMENT = "ACHIEVEMENT"
  val COMMENT = "COMMENT"
  val Notification = "Notification"
  val LEVELUP = "LEVELUP"
  val NOTIFICATION = "NOTIFICATION"
  val TEST = "TEST"

  def apply(`type`: String,
            title: String,
            text: String,
            owner: Submitter) = {
    val notification = new Notification()
    notification.`type` = `type`
    notification.title = title
    notification.text = text
    notification.owner = owner
    notification.creationTime = new js.Date()
    notification
  }

}