package com.socialized.javascript.models

import scala.scalajs.js

/**
  * Represents an entity that is similar, but more limited than, a user instance
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait UserLike extends js.Object {
  var _id: js.UndefOr[String]
  var firstName: js.UndefOr[String]
  var lastName: js.UndefOr[String]
}
