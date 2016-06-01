package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents an Emoticon
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Emoticon extends js.Object {
  var symbol: String = _
  var uri: String = _
  var tooltip: String = _
}

/**
  * Emoticon Companion Object
  * @author lawrence.daniels@gmail.com
  */
object Emoticon {

  def apply(symbol: String, uri: String, tooltip: String) = {
    val emoticon = new Emoticon()
    emoticon.symbol = symbol
    emoticon.uri = uri
    emoticon.tooltip = tooltip
    emoticon
  }

}