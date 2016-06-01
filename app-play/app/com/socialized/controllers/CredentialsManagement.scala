package com.socialized.controllers

import java.security.SecureRandom
import java.util.Date

import com.socialized.controllers.CredentialsManagement.EncryptionHelper
import com.socialized.models.Credential
import com.socialized.util.OptionHelper._
import org.mindrot.jbcrypt.BCrypt
import org.mindrot.jbcrypt.BCrypt._
import play.api.mvc.Controller

/**
  * Credentials Management
  * @author lawrence.daniels@gmail.com
  */
trait CredentialsManagement {
  self: Controller =>

  val encryptionHelper = EncryptionHelper()

  def performHash(credential: Credential, password: Option[String]) = {
    val encrypted = password.map(encryptionHelper.encrypt)
    credential.copy(hashSalt = encrypted.map(_.hashSalt), hashPassword = encrypted.map(_.hashPassword), creationTime = new Date())
  }

}

/**
  * Credentials Management Companion
  * @author lawrence.daniels@gmail.com
  */
object CredentialsManagement {

  /**
    * Encryption Helper
    */
  case class EncryptionHelper(logRounds: Int = 13) {

    def encrypt(password: String) = {
      val secureRandom = new SecureRandom()
      val hashSalt = gensalt(logRounds, secureRandom)
      val hashPassword = hashpw(password, hashSalt)
      Encrypted(hashSalt, hashPassword)
    }

    def compare(passwordA: String, passwordB: String) = BCrypt.checkpw(passwordA, passwordB)

  }

  /**
    * Encrypted Password
    */
  case class Encrypted(hashSalt: String, hashPassword: String)

}