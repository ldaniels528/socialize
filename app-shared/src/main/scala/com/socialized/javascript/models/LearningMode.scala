package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Learning Mode
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class LearningMode extends js.Object {
  var label: String = _
  var value: String = _
}

/**
  * Learning Mode Companion
  * @author lawrence.daniels@gmail.com
  */
object LearningMode {

  def apply(label: String, value: String) = {
    val mode = new LearningMode()
    mode.label = label
    mode.value = value
    mode
  }

}
