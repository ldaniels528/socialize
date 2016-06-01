package com.socialized.javascript.services

import com.github.ldaniels528.meansjs.angularjs.Service
import com.github.ldaniels528.meansjs.angularjs.http.Http
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
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
    $http.post[Session]("/api/signup", form).map(_.data)
  }

}
