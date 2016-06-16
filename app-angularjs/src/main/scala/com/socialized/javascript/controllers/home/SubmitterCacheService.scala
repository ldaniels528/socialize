package com.socialized.javascript.controllers.home

import org.scalajs.angularjs.AngularJsHelper._
import org.scalajs.angularjs.{Service, injected}
import com.socialized.javascript.models.{Post, Submitter, User}
import com.socialized.javascript.services.UserService
import org.scalajs.dom.browser.console

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

/**
  * Submitter Cache Service
  * @author lawrence.daniels@gmail.com
  */
class SubmitterCacheService(@injected("UserService") userService: UserService) extends Service {
  private val cache = js.Dictionary[Future[Submitter]]()

  /**
    * Asynchronously enriches the given posts with submitter instances
    * @param posts the given posts
    * @param ec    the given [[ExecutionContext execution context]]
    * @return a promise of the enriched posts
    */
  def enrich(posts: js.Array[Post])(implicit ec: ExecutionContext) = {
    val submitterIds = posts.flatMap(_.submitterId.toOption)
    for {
      submitters <- Future.sequence(submitterIds.toSeq.map(get(_)))
      submitterMap = Map(submitters.map(s => s._id.orNull -> s): _*)
    } yield {
      posts.foreach(post => post.submitter = post.submitterId.flatMap(submitterMap.get(_).orUndefined))
      posts
    }
  }

  /**
    * Asynchronously retrieves a submitter instance for the given submitter ID
    * @param submitterId the given submitter ID
    * @param ec          the given [[ExecutionContext execution context]]
    * @return a promise of the submitter instance
    */
  def get(submitterId: String)(implicit ec: ExecutionContext) = {
    cache.getOrElseUpdate(submitterId, {
      console.log(s"Loading submitter information for user # $submitterId...")
      val task = userService.getSubmitter(submitterId)
      task onFailure { case e =>
        console.log(s"Unexpected failure: ${e.displayMessage}")
        cache.delete(submitterId)
      }
      task
    })
  }

  /**
    * Asynchronously retrieves a submitter instance for the given user instance
    * @param user the submitting user
    * @param ec   the given [[ExecutionContext execution context]]
    * @return a promise of the submitter instance
    */
  def get(user: User)(implicit ec: ExecutionContext) = {
    user._id.toOption match {
      case Some(submitterId) => cache.getOrElseUpdate(submitterId, Future.successful(Submitter(user)))
      case None => Future.failed(new IllegalStateException("No submitter ID found"))
    }
  }

}
