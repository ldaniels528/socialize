package com.socialized.dao

import play.api.Logger

/**
  * Base class for all Data Access Object exceptions
  * @author lawrence.daniels@gmail.com
  */
class DAOException(message: String, cause: Throwable) extends RuntimeException(message, cause)

/**
  * DAO Exception Companion Object
  * @author lawrence.daniels@gmail.com
  */
object DAOException {

  def apply(message: String) = {
    Logger.info(s"message => $message")
    message match {
      case msg if msg.contains("duplicate key error") => new DuplicateKeyException(msg, null)
      case msg => new DAOException(msg, null)
    }
  }

}