package com.socialized.forms

import java.util.Date

import com.socialized.models.Credential
import com.socialized.util.OptionHelper._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Credential Form
  * @author lawrence.daniels@gmail.com
  */
case class CredentialForm(_id: Option[String],
                          username: Option[String],
                          hashPassword: Option[String],
                          hashSalt: Option[String],
                          creationTime: Option[Date]) {

  /**
    * Transforms the credential form into a credential model
    * @return the [[Credential credential model]]
    */
  def toModel = Credential(
    _id = _id.map(s => BSONObjectID(s)) ?? BSONObjectID.generate,
    username = username,
    hashPassword = hashPassword,
    hashSalt = hashSalt,
    creationTime = creationTime ?? new Date()
  )

}

/**
  * Credential Form Companion
  * @author lawrence.daniels@gmail.com
  */
object CredentialForm {

  /**
    * Credential Form Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class CredentialFormExtensions(val model: Credential) extends AnyVal {

    /**
      * Transforms the credential model into a credential form
      * @return the [[CredentialForm credential form]]
      */
    def toForm = CredentialForm(
      _id = model._id.map(_.stringify),
      username = model.username,
      hashPassword = model.hashPassword,
      hashSalt = model.hashSalt,
      creationTime = model.creationTime
    )

  }

  implicit val CredentialFormFormat = Json.format[CredentialForm]

}