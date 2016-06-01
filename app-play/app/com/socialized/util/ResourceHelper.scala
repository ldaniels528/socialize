package com.socialized.util

import scala.language.reflectiveCalls

/**
 * Resource Utilities
 * @author lawrence.daniels@gmail.com
 */
object ResourceHelper {

  /**
   * Facilitates the automatically closing of any resource that features
   * a <tt>close()<tt> method.
   */
  implicit class AutoClose[T <: {def close() : Unit}](val res: T) extends AnyVal {

    /**
     * Executes the block and closes the resource
     */
    def use[S](block: (T) => S): S = try block(res) finally res.close()

  }

  /**
   * Facilitates the automatically closing of any resource that features
   * a <tt>disconnect()<tt> method.
   */
  implicit class AutoDisconnect[T <: {def disconnect() : Unit}](val res: T) extends AnyVal {

    /**
     * Executes the block and closes the resource
     */
    def use[S](block: (T) => S): S = try block(res) finally res.disconnect()

  }

  /**
   * Facilitates the automatically closing of any resource that features
   * a <tt>disconnect()<tt> method.
   */
  implicit class AutoShutdown[T <: {def shutdown() : Unit}](val res: T) extends AnyVal {

    /**
     * Executes the block and closes the resource
     */
    def use[S](block: (T) => S): S = try block(res) finally res.shutdown()

  }

}
