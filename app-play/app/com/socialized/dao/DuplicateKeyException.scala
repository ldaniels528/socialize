package com.socialized.dao

/**
  * An exception class for Duplicate Key errors
  * @author lawrence.daniels@gmail.com
  */
class DuplicateKeyException(message: String, cause: Throwable) extends DAOException(message, cause)

/**
  * DAO Exception Companion Object
  * @author lawrence.daniels@gmail.com
  */
object DuplicateKeyException {

  def apply(message: String) = new DuplicateKeyException(message, null)

}