package com.socialized.javascript.forms

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Sign-Up Form
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class SignUpForm(var screenName: js.UndefOr[String] = js.undefined,
                 var firstName: js.UndefOr[String] = js.undefined,
                 var lastName: js.UndefOr[String] = js.undefined,
                 var password: js.UndefOr[String] = js.undefined,
                 var primaryEmail: js.UndefOr[String] = js.undefined,
                 var gender: js.UndefOr[String] = js.undefined) extends js.Object