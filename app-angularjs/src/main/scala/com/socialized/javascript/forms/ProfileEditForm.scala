package com.socialized.javascript.forms

import com.socialized.javascript.models.User

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  * Profile Edit Form
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class ProfileEditForm extends js.Object {
  var _id: js.UndefOr[String] = _
  var firstName: js.UndefOr[String] = _
  var lastName: js.UndefOr[String] = _
  var gender: js.UndefOr[String] = _
  var title: js.UndefOr[String] = _
  var company: js.UndefOr[String] = _
  var primaryEmail: js.UndefOr[String] = _
  var avatarURL: js.UndefOr[String] = _
}

/**
  * Edit Profile Form Companion
  * @author lawrence.daniels@gmail.com
  */
object ProfileEditForm {

  def apply(aUser: js.UndefOr[User]): ProfileEditForm = {
    val form = new ProfileEditForm()
    aUser foreach { user =>
      form._id = user._id
      form.firstName = user.firstName
      form.lastName = user.lastName
      form.primaryEmail = user.primaryEmail
      form.gender = user.gender
      form.title = user.title
      form.company = user.company
      form.avatarURL = user.avatarURL
    }
    form
  }

}