package com.socialized.javascript.models

import java.util.Date

import scala.scalajs.js

/**
  * Represents a File Attachment
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait Attachment extends js.Object {
  var _id: js.UndefOr[String] = js.native
  var filename: js.UndefOr[String] = js.native
  var contentType: js.UndefOr[String] = js.native
  var disposition: js.UndefOr[String] = js.native
  var uploadedDate: js.UndefOr[Date] = js.native
  var width: js.UndefOr[Double] = js.native
  var height: js.UndefOr[Double] = js.native

}
