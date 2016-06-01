package com.socialized.javascript

/**
  * String Helper
  * @author lawrence.daniels@gmail.com
  */
object StringHelper {

  /**
    * String Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class StringExtensions(val text: String) extends AnyVal {

    def extractAll(tok0: String, tok1: String, fromIndex: Int = 0): List[String] = text.findIndices(tok0, tok1, fromIndex) match {
      case Some((p0, p1)) => text.substring(p0 + tok0.length, p1 - tok1.length).trim :: text.extractAll(tok0, tok1, p1)
      case None => Nil
    }

    def findIndices(tok0: String, tok1: String) = for {
      start <- text.indexOptOf(tok0)
      end <- text.indexOptOf(tok1, start)
    } yield (start, end)

    def findIndices(tok0: String, tok1: String, fromIndex: Int) = for {
      start <- text.indexOptOf(tok0, fromIndex)
      end <- text.indexOptOf(tok1, start)
    } yield (start, end)

    def indexOptOf(s: String) = text.indexOf(s) match {
      case -1 => None
      case index => Some(index)
    }

    def indexOptOf(s: String, fromIndex: Int) = text.indexOf(s, fromIndex) match {
      case -1 => None
      case index => Some(index)
    }

    def unquote = text match {
      case s if s.startsWith("\"") && s.endsWith("\"") => s.drop(1).dropRight(1)
      case s => s
    }

  }

}
