package com.socialized.javascript.services

import org.scalajs.angularjs.Service
import org.scalajs.angularjs.http.Http
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.forms.SignUpForm
import com.socialized.javascript.models.Session

import scala.concurrent.ExecutionContext

/**
  * Sign-Up Service (AngularJS)
  * @author lawrence.daniels@gmail.com
  */
class SignUpService($http: Http) extends Service {

  /**
    * Creates a new user account
    * @param form the given [[com.socialized.javascript.forms.SignUpForm form]]
    * @return a promise of the newly created [[Session session]]
    */
  def createAccount(form: SignUpForm)(implicit ec: ExecutionContext) = {
    $http.post[Session]("/api/signup", form)
  }

}
