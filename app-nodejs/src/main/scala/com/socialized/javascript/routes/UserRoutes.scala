package com.socialized.javascript.routes

import org.scalajs.nodejs.NodeRequire
import org.scalajs.nodejs.express.{Application, Request, Response}
import org.scalajs.nodejs.mongodb._
import org.scalajs.nodejs.request.{Request => RequestClient}
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.data.UserDAO._
import com.socialized.javascript.data.UserData._
import com.socialized.javascript.data.{UserDAO, UserData}
import com.socialized.javascript.models.User

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.util.{Failure, Success}

/**
  * User Routes
  * @author lawrence.daniels@gmail.com
  */
object UserRoutes {

  def init(app: Application, dbFuture: Future[Db])(implicit ec: ExecutionContext, mongo: MongoDB, require: NodeRequire) = {
    implicit val userDAO = dbFuture.flatMap(_.getUserDAO)
    implicit val client = require[RequestClient]("request")

    // User CRUD
    app.post("/api/user", (request: Request, response: Response, next: NextFunction) => createUser(request, response, next))
    app.get("/api/user/:userID", (request: Request, response: Response, next: NextFunction) => getUserByID(request, response, next))
    app.put("/api/user/:userID", (request: Request, response: Response, next: NextFunction) => updateUser(request, response, next))
    app.get("/api/user/email", (request: Request, response: Response, next: NextFunction) => getUserByEmail(request, response, next))
    app.get("/api/user/name", (request: Request, response: Response, next: NextFunction) => getUserByUsername(request, response, next))

    // Avatars
    app.get("/api/user/:userID/avatar", (request: Request, response: Response, next: NextFunction) => getAvatarByID(request, response, next))
    app.get("/api/user/:userID/submitter", (request: Request, response: Response, next: NextFunction) => getSubmitter(request, response, next))

    // Likers
    app.get("/api/user/:endorseeID/endorsers", (request: Request, response: Response, next: NextFunction) => getEndorsers(request, response, next))
    app.put("api/user/:endorseeID/like/:endorserID", (request: Request, response: Response, next: NextFunction) => likeUser(request, response, next))
    app.delete("api/user/:endorseeID/like/:endorserID", (request: Request, response: Response, next: NextFunction) => unlikeUser(request, response, next))

    // Followers
    app.get("/api/user/:followeeID/followers", (request: Request, response: Response, next: NextFunction) => getFollowers(request, response, next))
    app.put("/api/user/:followeeID/follow/:followerID", (request: Request, response: Response, next: NextFunction) => followUser(request, response, next))
    app.delete("/api/user/:followeeID/follow/:followerID", (request: Request, response: Response, next: NextFunction) => unfollowUser(request, response, next))
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      User CRUD
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Creates a new user
    * @example POST /api/user [User in body]
    */
  def createUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    request.bodyAs[User].toData match {
      case Success(user) =>
        userDAO.flatMap(_.insert(user)) onComplete {
          case Success(result) if result.isOk => response.send(result.ops.headOption.orNull); next()
          case Success(result) => response.badRequest("Post could not be created"); next()
          case Failure(e) => response.internalServerError(e); next()
        }
      case Failure(e) => response.badRequest(e); next()
    }
  }

  /**
    * Retrieve a user by ID
    * @example GET /api/user/5633c756d9d5baa77a714803
    */
  def getUserByID(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val userID = request.params("userID")
    userDAO.flatMap(_.findById[User](userID)) onComplete {
      case Success(Some(user)) => response.send(user); next()
      case Success(None) => response.notFound(); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve a user by email address
    * @example GET /api/user/email?email=some.one@somewhere.com
    */
  def getUserByEmail(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    request.query.get("primaryEmail") match {
      case Some(primaryEmail) =>
        userDAO.flatMap(_.findOneFuture[User]("primaryEmail" $eq primaryEmail)) onComplete {
          case Success(Some(user)) => response.send(user); next()
          case Success(None) => response.notFound(primaryEmail); next()
          case Failure(e) => response.internalServerError(e); next()
        }
      case None => response.badRequest("param 'primaryEmail' required"); next()
    }
  }

  /**
    * Retrieve a user by username
    * @example GET /api/user/name?username=ldaniels528
    */
  def getUserByUsername(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    request.query.get("username") match {
      case Some(username) =>
        userDAO.flatMap(_.findOneFuture[User]("screenName" $eq username)) onComplete {
          case Success(Some(user)) => response.send(user); next()
          case Success(None) => response.notFound(username); next()
          case Failure(e) => response.internalServerError(e); next()
        }
      case None => response.badRequest("param 'username' required"); next()
    }
  }

  /**
    * Update a user by ID
    * @example GET /api/user/5633c756d9d5baa77a714803
    */
  def updateUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val userID = request.params("userID")
    request.bodyAs[User].toData match {
      case Success(user) =>
        userDAO.flatMap(_.findById[User](userID)) onComplete {
          case Success(Some(updatedUser)) => response.send(updatedUser); next()
          case Success(None) => response.notFound(); next()
          case Failure(e) => response.internalServerError(e); next()
        }
      case Failure(e) => response.badRequest(e); next()
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Avatars, etc.
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Retrieves a user's avatar by ID
    * @example GET /api/user/5633c756d9d5baa77a714803/avatar
    */
  def getAvatarByID(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO], client: RequestClient) = {
    val (userID, host) = (request.params("userID"), request.headers("host"))
    userDAO.flatMap(_.findById[User](userID)) onComplete {
      case Success(Some(user)) =>
        val avatarId = user.avatarId.toOption
        val avatarURL = user.avatarURL.toOption
        avatarURL match {
          case Some(url) => client.get(url).pipe(response).onEnd(() => next())
          case None => client.get(s"http://$host/images/avatars/anonymous.png").pipe(response).onEnd(() => next())
        }
      case Success(None) => client.get(s"http://$host/images/avatars/anonymous.png").pipe(response).onEnd(() => next())
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve a submitter by ID
    * @example GET /api/user/5633c756d9d5baa77a714803/submitter
    */
  def getSubmitter(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val userID = request.params("userID")
    userDAO.flatMap(_.findById[UserData](userID, js.Array("avatarURL", "firstName", "lastName"))) onComplete {
      case Success(Some(user)) => response.send(user.toSubmitter); next()
      case Success(None) => response.notFound(userID); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Endorsers (Like / UnLike)
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Retrieve a user's endorsers by ID
    * @example GET /api/user/5633c756d9d5baa77a714803/endorsers
    */
  def getEndorsers(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val endorseeID = request.params("endorseeID")
    userDAO.flatMap(_.find("likedBy" $in js.Array(endorseeID)).toArrayFuture[User]) onComplete {
      case Success(followers) => response.send(followers); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Like/endorse a user by user ID
    * @example PUT /api/user/5633c756d9d5baa77a714803/like/564047821b591f08aeaa40b4
    */
  def likeUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val (endorseeID, endorserID) = (request.params("endorseeID"), request.params("endorserID"))
    userDAO.flatMap(_.like(endorseeID, endorserID)) onComplete {
      case Success(result) if result.isOk => response.send(result.value); next()
      case Success(result) => response.badRequest("Endorsement failed"); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retracts the endorsement (UnLike) a user by user ID
    * @example PUT /api/user/5633c756d9d5baa77a714803/like/564047821b591f08aeaa40b4
    */
  def unlikeUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val (endorseeID, endorserID) = (request.params("endorseeID"), request.params("endorserID"))
    userDAO.flatMap(_.unlike(endorseeID, endorserID)) onComplete {
      case Success(result) if result.isOk => response.send(result.value); next()
      case Success(result) => response.badRequest("De-Endorsement failed"); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /////////////////////////////////////////////////////////////////////////////////
  //      Followers (Follow / UnFollow)
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Follow a user by user ID
    * @example PUT /api/user/5633c756d9d5baa77a714803/like/564047821b591f08aeaa40b4
    */
  def followUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val (followeeID, followerID) = (request.params("followeeID"), request.params("followerID"))
    userDAO.flatMap(_.follow(followeeID, followerID)) onComplete {
      case Success(result) if result.isOk => response.send(result.value); next()
      case Success(result) => response.badRequest("Start following failed"); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Retrieve a user's followers by ID
    * @example GET /api/user/5633c756d9d5baa77a714803/followers
    */
  def getFollowers(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val followeeID = request.params("followeeID")
    userDAO.flatMap(_.find("followers" $in js.Array(followeeID)).toArrayFuture[User]) onComplete {
      case Success(followers) => response.send(followers); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

  /**
    * Un-Follow a user by user ID
    * @example PUT /api/user/5633c756d9d5baa77a714803/like/564047821b591f08aeaa40b4
    */
  def unfollowUser(request: Request, response: Response, next: NextFunction)(implicit ec: ExecutionContext, mongo: MongoDB, userDAO: Future[UserDAO]) = {
    val (followeeID, followerID) = (request.params("followeeID"), request.params("followerID"))
    userDAO.flatMap(_.unfollow(followeeID, followerID)) onComplete {
      case Success(result) if result.isOk => response.send(result.value); next()
      case Success(result) => response.badRequest("Stop following failed"); next()
      case Failure(e) => response.internalServerError(e); next()
    }
  }

}
