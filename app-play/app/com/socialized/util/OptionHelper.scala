package com.socialized.util

import scala.language.implicitConversions

/**
  * Option Helper
  * @author lawrence.daniels@gmail.com
  */
object OptionHelper {

  /**
    * Implicit conversion to transform a naked value into an option of the value
    * @param value the given naked value
    * @tparam T the template type
    * @return the option of the value
    */
  implicit def value2Option[T](value: T): Option[T] = Option(value)

  /**
    * Syntactic sugar for frequently needed helper functions
    * @param opt the given [[scala.Option option]]
    * @tparam T the template type
    * @author lawrence.daniels@gmail.com
    */
  implicit class OptionEnrichment[T](val opt: Option[T]) extends AnyVal {

    def ??(optB: Option[T]): Option[T] = if(opt.isDefined) opt else optB

    def orDie(message: String): T = opt.getOrElse(throw new IllegalStateException(message))

  }

  /**
    * Implicit conversions
    * @author lawrence.daniels@gmail.com
    */
  object Implicits {

    implicit def value2Option[T](value: T): Option[T] = Option(value)

    implicit def stringOption2Value(value: Option[String]): String = value.orNull

  }

}
