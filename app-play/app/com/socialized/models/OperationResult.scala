package com.socialized.models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

/**
  * Operation Result
  * @author lawrence.daniels@gmail.com
  */
case class OperationResult(success: Boolean, message: Option[String] = None)

/**
  * Operation Result Companion
  * @author lawrence.daniels@gmail.com
  */
object OperationResult {

  implicit val OperationResultFormat = Json.format[OperationResult]

  implicit val OperationResultHandler = Macros.handler[OperationResult]

}
