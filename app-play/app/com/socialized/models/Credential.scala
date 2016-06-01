package com.socialized.models

import java.util.Date

import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.play.json.BSONFormats.{BSONObjectIDFormat => BSONIDF}
import reactivemongo.bson._

/**
 * Represents an authentication credential
 * @author lawrence.daniels@gmail.com
 */
case class Credential(_id: Option[BSONObjectID] = BSONObjectID.generate,
                      username: Option[String],
                      hashPassword: Option[String] = None,
                      hashSalt: Option[String] = None,
                      creationTime: Option[Date] = new Date())

/**
 * Credential Companion
 * @author lawrence.daniels@gmail.com
 */
object Credential {

  implicit val CredentialFormat = Json.format[Credential]

  implicit val CredentialHandler = Macros.handler[Credential]

  /**
   * Credential Enrichment Helper
   * @param credential the given [[Credential credential]]
   */
  implicit class CredentialEnrichment(val credential: Credential) extends AnyVal {

    def clearHash = credential.copy(hashPassword = None, hashSalt = None)

  }

}