package com.socialized.javascript.ui

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a Menu Item
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class MenuItem(var text: String = null,
               var iconClass: String = null,
               var action: js.Function = null,
               var include: String = null) extends js.Object

/**
  * Menu Item Companion
  * @author lawrence.daniels@gmail.com
  */
object MenuItem {

  def apply(text: String, iconClass: String, action: js.Function = null) = {
    new MenuItem(text = text, iconClass = iconClass, action = action)
  }

  def include(src: String) = new MenuItem(include = src)

}