package com.socialized.javascript.models

import scala.scalajs.js

/**
 * Represents an Authentication Credential
 * @author lawrence.daniels@gmail.com
 */
@js.native
trait Credential extends js.Object {
  var _id: js.UndefOr[String] = js.native
  var username: js.UndefOr[String] = js.native
  var creationTime: js.UndefOr[js.Date] = js.native
}
