package com.socialized.javascript.models

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Operation Result
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class OperationResult(var success: Boolean = false,
                      var message: js.UndefOr[String] = js.undefined) extends js.Object