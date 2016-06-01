package com.socialized.javascript.ui

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Represents a Menu
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class Menu extends js.Object {
  var text: js.UndefOr[String] = _
  var items: js.Array[MenuItem] = _
}

/**
  * Menu Companion
  * @author lawrence.daniels@gmail.com
  */
object Menu {

  def apply(text: String, items: js.Array[MenuItem]) = {
    val menu = new Menu()
    menu.text = text
    menu.items = items
    menu
  }

}