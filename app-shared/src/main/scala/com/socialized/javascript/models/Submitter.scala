package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a Submitter model object
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Submitter(var _id: js.UndefOr[String] = js.undefined,
                var name: js.UndefOr[String] = js.undefined,
                var avatarURL: js.UndefOr[String] = js.undefined) extends js.Object

/**
  * Submitter Companion
  * @author lawrence.daniels@gmail.com
  */
object Submitter {

  def apply(user: User): Submitter = {
    val submitter = new Submitter()
    submitter._id = user._id
    submitter.name = user.fullName
    submitter.avatarURL = user.avatarURL
    submitter
  }

}