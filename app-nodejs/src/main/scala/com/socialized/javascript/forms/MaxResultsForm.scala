package com.socialized.javascript.forms

import scala.scalajs.js

/**
  * Max Results Form
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait MaxResultsForm extends js.Object {
  var maxResults: js.UndefOr[String] = js.native
}

/**
  * Max Results Form Companion
  * @author lawrence.daniels@gmail.com
  */
object MaxResultsForm {

  /**
    * Max Results Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class MaxResultsFormExtensions(val form: MaxResultsForm) extends AnyVal {

    def getMaxResults(default: Int = 20): Int = form.maxResults.map(Integer.parseInt) getOrElse default

  }

}