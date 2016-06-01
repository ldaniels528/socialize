package com.socialized.javascript.util

import scala.scalajs.js

/**
  * Tag Helper Utility
  * @author lawrence.daniels@gmail.com
  */
object TagHelper {

  def composeList(words: js.Array[String]) = {
    val args = words.map(s => s.head.toUpper + s.tail)
    if (args.length == 1) args.head else args.init.mkString(", ") + " and " + args.last
  }

}
