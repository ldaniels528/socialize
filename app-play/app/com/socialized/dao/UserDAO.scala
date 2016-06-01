package com.socialized.dao

import com.socialized.dao.mongodb.UserDAOMongoDB
import com.socialized.forms.ProfileEditForm
import com.socialized.models.{EntitySearchResult, Submitter, User}
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

/**
  * Represents a User DAO
  * @author lawrence.daniels@gmail.com
  */
trait UserDAO {

  /**
    * Retrieves user by username
    * @param username the given username
    * @param ec       the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of an option of a [[User user]]
    */
  def findByUsername(username: String)(implicit ec: ExecutionContext): Future[Option[User]]

  /**
    * Retrieves user by primary email address
    * @param primaryEmail the given primary email address
    * @param ec           the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of an option of a [[User user]]
    */
  def findByEmail(primaryEmail: String)(implicit ec: ExecutionContext): Future[Option[User]]

  /**
    * Retrieves all endorses for a given user (endorsee)
    * @param endorseeID the given user's (endorsee) ID
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[User users]]
    */
  def findEndorsers(endorseeID: String)(implicit ec: ExecutionContext): Future[List[User]]

  /**
    * Retrieves all follower for a given user (followee)
    * @param followeeID the given user's (followee) ID
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[User users]]
    */
  def findFollowers(followeeID: String)(implicit ec: ExecutionContext): Future[List[User]]

  /**
    * Retrieves a list of submitters by ID
    * @param submitterIds the list of submitter IDs
    * @param ec           the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[Submitter submitter]]
    */
  def findSubmitters(submitterIds: Seq[String])(implicit ec: ExecutionContext): Future[List[Submitter]]

  /**
    * Sets the "follow this user" indicator
    * @param id         the user ID of the followee
    * @param followerID the user ID of the follower
    * @param ec         the implicit [[ExecutionContext execution context]]
    * @return a promise of the updated user model (followee)
    */
  def follow(id: String, followerID: String)(implicit ec: ExecutionContext): Future[Option[User]]

  /**
    * Sets the "unfollow this user" indicator
    * @param id         the user ID of the followee
    * @param followerID the user ID of the follower
    * @param ec         the implicit [[ExecutionContext execution context]]
    * @return a promise of the updated user model (followee)
    */
  def unfollow(id: String, followerID: String)(implicit ec: ExecutionContext): Future[Option[User]]

  /**
    * Performs a search for users matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  def search(searchTerm: String)(implicit ec: ExecutionContext): Future[List[EntitySearchResult]]

  /**
    * Updates a user with the content of the given form
    * @param form the given [[ProfileEditForm form]]
    * @param ec   the given [[ExecutionContext Execution Context]]
    * @return a promise of an option of an updated [[User user]]
    */
  def update(form: ProfileEditForm)(implicit ec: ExecutionContext): Future[Option[User]]

}

/**
  * User DAO Companion
  * @author lawrence.daniels@gmail.com
  */
object UserDAO {

  def apply(reactiveMongoApi: ReactiveMongoApi) = {
    new UserDAOMongoDB(reactiveMongoApi)
  }

}