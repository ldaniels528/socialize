package com.socialized.javascript.data

import io.scalajs.npm.mongodb.{Collection, Db}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Question DAO
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait QuestionDAO extends Collection

/**
  * Question DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object QuestionDAO {

  /**
    * Question DAO Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class QuestionDAOExtensions(val db: Db) extends AnyVal {

    def getQuestionDAO(implicit ec: ExecutionContext) = db.collectionFuture("questions").map(_.asInstanceOf[QuestionDAO])

  }

}