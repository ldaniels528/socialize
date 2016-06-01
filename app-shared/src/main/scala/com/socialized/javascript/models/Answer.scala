package com.socialized.javascript.models

import scala.scalajs.js

/**
  * Represents an answer model object
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait Answer extends js.Object {
  var _id: js.UndefOr[String]
  var text: js.UndefOr[String]
  var correct: js.UndefOr[Boolean]

}
