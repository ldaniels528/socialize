package com.socialized.javascript.models

import scala.scalajs.js

/**
  * Represents Shared Content; usually posted from a news site, etc.
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait SharedContent extends js.Object {
  var author: js.UndefOr[String]
  var description: js.UndefOr[String]
  var locale: js.UndefOr[String]
  var publishedTime: js.UndefOr[js.Date]
  var section: js.UndefOr[String]
  var source: js.UndefOr[String]
  var tags: js.UndefOr[js.Array[String]]
  var title: js.UndefOr[String]
  var thumbnailUrl: js.UndefOr[String]
  var updatedTime: js.UndefOr[js.Date]
  var url: js.UndefOr[String]

}
