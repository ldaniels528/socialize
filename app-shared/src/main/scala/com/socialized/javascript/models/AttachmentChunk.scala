package com.socialized.javascript.models

import scala.scalajs.js

/**
  * Represents a File Attachment Chunk
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait AttachmentChunk extends js.Object {
  var _id: js.UndefOr[String]
  var files_id: js.UndefOr[String]
  var n: js.UndefOr[Int]
  var data: js.Any
}
