package com.socialized.javascript

import com.socialized.javascript.controllers._
import com.socialized.javascript.controllers.home._
import com.socialized.javascript.directives.{CareerTalkDirective, CompileDirective}
import com.socialized.javascript.services._
import org.scalajs.angularjs.Module.EnrichedModule
import org.scalajs.angularjs.http.HttpProvider
import org.scalajs.angularjs.uirouter.{RouteProvider, RouteTo}
import org.scalajs.angularjs.{Scope, angular}
import org.scalajs.dom.browser.console

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/**
  * Socialized Scala.js Application
  * @author lawrence.daniels@gmail.com
  */
@JSExport
object SocializedJSApp extends js.JSApp {

  @JSExport
  override def main() {
    // create the application
    val module = angular.createModule("Socialized", js.Array(
      "ngAnimate", "ngCookies", "ngRoute", "ngSanitize", "angularFileUpload", "toaster", "ui.bootstrap"))

    // configure the Angular.js components
    configureControllers(module)
    configureDirectives(module)
    configureFilters(module)
    configureServices(module)

    // configure the application (routes, etc.)
    configureApplication(module)

    // initialize the application
    module.run({ ($rootScope: Scope) =>
      console.log("Initializing Socialized...")
    })
  }

  private def configureControllers(module: EnrichedModule): Unit = {
    module.controllerOf[AuthenticationController]("AuthenticationController")
    module.controllerOf[EventController]("EventController")
    module.controllerOf[HomeController]("HomeController")
    module.controllerOf[MainController]("MainController")
    module.controllerOf[PhotosController]("PhotosController")
    module.controllerOf[ProfileController]("ProfileController")
    module.controllerOf[ProfileEditorController]("ProfileEditorController")
    module.controllerOf[SignUpController]("SignUpController")
  }

  private def configureDirectives(module: EnrichedModule): Unit = {
    module.directiveOf[CareerTalkDirective]("careertalk")
    module.directiveOf[CompileDirective]("compileA")
    //module.directiveOf[NgThumbDirective]("ngThumb")
  }

  private def configureFilters(module: EnrichedModule): Unit = {
    module.filter("capitalize", Filters.capitalize)
    module.filter("duration", Filters.duration)
    module.filter("yesno", Filters.yesNo)
  }

  private def configureServices(module: EnrichedModule): Unit = {
    module.serviceOf[AuthenticationService]("AuthenticationService")
    module.serviceOf[EventService]("EventService")
    module.serviceOf[GroupService]("GroupService")
    module.serviceOf[InboxMessageService]("InboxMessageService")
    module.serviceOf[MySessionService]("MySession")
    module.serviceOf[NotificationService]("NotificationService")
    module.serviceOf[PostService]("PostService")
    module.serviceOf[ReactiveSearchService]("ReactiveSearchService")
    module.serviceOf[SignUpService]("SignUpService")
    module.serviceOf[SubmitterCacheService]("SubmitterCache")
    module.serviceOf[UserService]("UserService")
    module.serviceOf[UserSessionService]("UserSessionService")
    module.serviceOf[WebSocketService]("WebSocketService")
  }

  private def configureApplication(module: EnrichedModule): Unit = {
    module.config({ ($httpProvider: HttpProvider, $routeProvider: RouteProvider) =>
      // enable cross domain calls
      $httpProvider.defaults("useXDomain") = true

      // remove the header used to identify AJAX call that would prevent CORS from working
      for {
        headers <- $httpProvider.defaults.get("headers").map(_.asInstanceOf[js.Dictionary[js.Any]])
        common <- headers.get("common").map(_.asInstanceOf[js.Dictionary[js.Any]])
      } common.remove("X-Requested-With")

      // configure the application routes
      $routeProvider
        .when("/home", RouteTo(redirectTo = "/home/newsfeed"))
        .when("/home/events", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/home/messages", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/home/newsfeed", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/home/photos", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/home/profile/edit", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/home/profile/viewer/:id", RouteTo(templateUrl = "/assets/views/home/index.html"))
        .when("/login", RouteTo(templateUrl = "/assets/views/login/index.html"))
        .otherwise(RouteTo(redirectTo = "/home"))
      ()
    })
  }

}
