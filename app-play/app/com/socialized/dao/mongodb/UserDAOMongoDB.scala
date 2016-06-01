package com.socialized.dao.mongodb

import com.socialized.dao.UserDAO
import com.socialized.forms.ProfileEditForm
import com.socialized.models.{EntitySearchResult, Submitter, User}
import com.socialized.util.OptionHelper._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.{BSONArray, BSONObjectID, BSONString, BSONDocument => BS}

import scala.concurrent.ExecutionContext

/**
  * User DAO (MongoDB implementation)
  * @author lawrence.daniels@gmail.com
  */
class UserDAOMongoDB(val reactiveMongoApi: ReactiveMongoApi) extends UserDAO
  with EntityMaintenanceDAO[User] with LikeableEntityDAO[User] with EntitySearchDAO[User] {

  override val collectionName = "users"

  /**
    * Retrieves user by username
    * @param username the given username
    * @param ec       the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of an option of a [[User user]]
    */
  override def findByUsername(username: String)(implicit ec: ExecutionContext) = {
    findOneBy("screenName" -> username)
  }

  /**
    * Retrieves user by primary email address
    * @param primaryEmail the given primary email address
    * @param ec           the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of an option of a [[User user]]
    */
  override def findByEmail(primaryEmail: String)(implicit ec: ExecutionContext) = {
    findOneBy("primaryEmail" -> primaryEmail)
  }

  /**
    * Retrieves all endorses for a given user (endorsee)
    * @param endorseeID the given user's (endorsee) ID
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[User users]]
    */
  override def findEndorsers(endorseeID: String)(implicit ec: ExecutionContext) = {
    findMatches("likedBy" -> BS("$in" -> BSONArray(endorseeID)))
  }

  /**
    * Retrieves all follower for a given user (followee)
    * @param followeeId the given user's (followee) ID
    * @param ec         the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[User users]]
    */
  override def findFollowers(followeeId: String)(implicit ec: ExecutionContext) = {
    findMatches("followers" -> BS("$in" -> BSONArray(followeeId)))
  }

  /**
    * Retrieves a list of submitters by ID
    * @param submitterIds the list of submitter IDs
    * @param ec           the given [[scala.concurrent.ExecutionContext Execution Context]]
    * @return a promise of a collections of a [[Submitter submitter]]
    */
  override def findSubmitters(submitterIds: Seq[String])(implicit ec: ExecutionContext) = {
    findMatches("_id" -> BS("$in" -> BSONArray(submitterIds.map(BSONObjectID(_))))) map (_ map (u => Submitter(u._id.map(_.stringify), u.fullName, u.avatarURL)))
  }

  /**
    * Sets the "follow this user" indicator
    * @param id         the user ID of the followee
    * @param followerID the user ID of the follower
    * @param ec         the implicit [[ExecutionContext execution context]]
    * @return a promise of the updated user model (followee)
    */
  override def follow(id: String, followerID: String)(implicit ec: ExecutionContext) = {
    likeSpecial(id, followerID, collectionName = "followers", collectionQtyName = "totalFollowers")
  }

  /**
    * Sets the "unfollow this user" indicator
    * @param id         the user ID of the followee
    * @param followerID the user ID of the follower
    * @param ec         the implicit [[ExecutionContext execution context]]
    * @return a promise of the updated user model (followee)
    */
  override def unfollow(id: String, followerID: String)(implicit ec: ExecutionContext) = {
    likeSpecial(id, followerID, collectionName = "followers", collectionQtyName = "totalFollowers")
  }

  /**
    * Performs a search for users matching the given search term
    * @param searchTerm the given search term
    * @param ec         the given [[ExecutionContext Execution Context]]
    * @return a promise of a [[List]] of [[EntitySearchResult search results]]
    */
  override def search(searchTerm: String)(implicit ec: ExecutionContext) = {
    doSearch(searchTerm, "firstName", "lastName", "primaryEmail") { u =>
      EntitySearchResult(_id = u._id, name = u.fullName, description = u.company, avatarURL = u.avatarURL, `type` = "USER")
    }
  }

  /**
    * Updates a user with the content of the given form
    * @param form the given [[ProfileEditForm form]]
    * @param ec   the given [[ExecutionContext Execution Context]]
    * @return a promise of an option of an updated [[User user]]
    */
  override def update(form: ProfileEditForm)(implicit ec: ExecutionContext) = {
    patch(form._id,
      "$set" -> BS(
        "firstName" -> form.firstName,
        "lastName" -> form.lastName,
        "primaryEmail" -> form.primaryEmail,
        "gender" -> form.gender,
        "company" -> form.company,
        "title" -> form.title,
        "avatarURL" -> form.avatarURL)
    )
  }

}
