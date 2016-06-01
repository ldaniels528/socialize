package com.socialized.javascript.ui

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Main Tab
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class MainTab extends js.Object {
  var name: String = _
  var url: String = _
  var icon: String = _
  var hover: UndefOr[Boolean] = _
}

/**
  * Main Tab Singleton
  * @author lawrence.daniels@gmail.com
  */
object MainTab {

  def apply(name: String, url: String, icon: String) = {
    val tab = new MainTab()
    tab.name = name
    tab.url = url
    tab.icon = icon
    tab
  }
}