package com.socialized.javascript.data

import scala.scalajs.js

/**
  * Credential Data Object
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait CredentialData extends js.Object {
  var _id: js.UndefOr[String] = js.native
  var username: js.UndefOr[String] = js.native
  var hashPassword: js.UndefOr[String] = js.native
  var hashSalt: js.UndefOr[String] = js.native
  var creationTime: js.UndefOr[js.Date] = js.native
}
