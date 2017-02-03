package com.socialized.javascript.util

import io.scalajs.dom.html.browser.encodeURI

/**
  * Query Helper
  * @author lawrence.daniels@gmail.com
  */
object QueryHelper {

  def params(values: (String, Any)*): String = {
    val queryString = values map { case (k, v) => s"$k=${encodeURI(String.valueOf(v))}" } map (_.replaceAllLiterally("&", "%26")) mkString "&"
    if (queryString.nonEmpty) "?" + queryString else queryString
  }

}
