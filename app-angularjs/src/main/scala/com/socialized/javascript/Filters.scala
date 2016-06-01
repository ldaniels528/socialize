package com.socialized.javascript

import com.github.ldaniels528.meansjs.angularjs.angular
import com.github.ldaniels528.meansjs.core.console
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._

import scala.language.postfixOps
import scala.scalajs.js
import scala.scalajs.js.Any._

/**
  * Skilled.com AngularJS Filter
  * @author lawrence.daniels@gmail.com
  */
object Filters {
  private val timeUnits = Seq("min", "hour", "day", "month", "year")
  private val timeFactors = Seq(60, 24, 30, 12)

  /**
    * Capitalize: Returns the capitalize representation of a given string
    */
  val capitalize: js.Function = () => { (value: js.UndefOr[String]) =>
    value map { s => if (s.nonEmpty) s.head.toUpper + s.tail else "" }
  }: js.Function

  /**
    * Duration: Converts a given time stamp to a more human readable expression (e.g. "5 mins ago")
    */
  val duration: js.Function = () => { (time: js.UndefOr[js.Any]) => toDuration(time, noFuture = false) }: js.Function

  /**
    * Yes/No: Converts a boolean value into 'Yes' or 'No'
    */
  val yesNo: js.Function = () => ((state: Boolean) => if (state) "Yes" else "No"): js.Function

  /**
    * Converts the given time expression to a textual duration
    * @param aTime the given time (date string or value in milliseconds)
    * @return the duration (e.g. "10 mins ago")
    */
  def toDuration(aTime: js.UndefOr[js.Any], noFuture: Boolean = false) = {
    // get the time in milliseconds
    val aTimeMillis = aTime.flat.map {
      case v if angular.isString(v) => js.Date.parse(v.asInstanceOf[String])
      case v if angular.isDate(v) => v.asInstanceOf[js.Date].getDate().toDouble
      case v if angular.isNumber(v) => v.asInstanceOf[Number].doubleValue()
      case v =>
        console.log("v =", v, v.getClass.getName)
        js.Date.now()
    }

    aTimeMillis map { timeMillis =>
      // compute the elapsed time in minutes
      val elapsedMinutes = ((js.Date.now() - timeMillis) / 60000d).toLong

      // compute the age
      var age = elapsedMinutes
      var unit = 0
      while (unit < timeFactors.length && age >= timeFactors(unit)) {
        age /= timeFactors(unit)
        unit += 1
      }

      // make the age and unit names more readable
      val unitName = timeUnits(unit) + (if (age.toInt != 1) "s" else "")
      if (unit == 0 && (age >= 0 && age < 1)) "just now"
      else if (elapsedMinutes < 0) {
        if (noFuture) "moments ago" else s"$age $unitName from now"
      }
      else s"$age $unitName ago"
    }
  }

}
