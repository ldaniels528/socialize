package com.socialized.util

/**
  * String Helper
  * @author lawrence.daniels@gmail.com
  */
object StringHelper {
  private[this] val RegExValidEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

  /**
    * String Enrichment
    * @param host the given host string
    */
  implicit class StringEnrichment(val host: String) extends AnyVal {

    def indexOptOf(s: String) = host.indexOf(s) match {
      case -1 => None
      case index => Some(index)
    }

    def isValidEmail = host.matches(RegExValidEmail)

  }

}
