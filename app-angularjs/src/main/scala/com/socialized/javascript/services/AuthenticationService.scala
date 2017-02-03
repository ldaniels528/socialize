package com.socialized.javascript.services

import io.scalajs.npm.angularjs.Service
import io.scalajs.npm.angularjs.http.Http
import io.scalajs.util.ScalaJsHelper._
import com.socialized.javascript.forms.LoginForm
import com.socialized.javascript.models.{Credential, Session}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

/**
  * Authentication Service
  * @author lawrence.daniels@gmail.com
  */
class AuthenticationService($http: Http) extends Service {

  /**
    * Attempts to authenticate a user
    * @param loginForm the given [[LoginForm login credentials]]
    * @return a promise of an authenticated [[Session session]]
    */
  def authenticate(loginForm: LoginForm)(implicit ec: ExecutionContext) = {
    $http.post[Session]("/api/signin", loginForm)
  }

  /**
    * Retrieves an credential by ID
    * @param id the given credential ID
    * @return a promise of an [[Credential credential]]
    */
  def getCredentialByID(id: String)(implicit ec: ExecutionContext) = {
    $http.get[Credential](s"/api/credential/$id")
  }

  /**
    * Retrieves all active credentials
    * @return a promise of the array of [[Credential credentials]]
    */
  def getCredentials(implicit ec: ExecutionContext) = {
    $http.get[js.Array[Credential]]("/api/credentials")
  }

  /**
    * Creates a new user authentication credential
    * @param username the given username
    * @param password the given password
    * @return a promise of the newly created credential
    */
  def createCredential(username: String, password: String)(implicit ec: ExecutionContext) = {
    $http.post[Credential]("/api/credentials", LoginForm(username, password))
  }

}

