package com.socialized.javascript.models

import scala.scalajs.js

/**
  * Represents the Like action for a Reply
  * <b>NOTE</b>: due to a MongoDB restriction for deeply nested objects,
  * likes could not be included in the [[Reply reply class]]
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait ReplyLikes extends js.Object{
  var _id: js.UndefOr[String]
  var likes: js.UndefOr[Int]
  var likedBy: js.UndefOr[js.Array[String]]
}
