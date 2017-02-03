package com.socialized.javascript

import com.socialized.javascript.routes._
import io.scalajs.nodejs.{process, _}
import io.scalajs.npm.bodyparser._
import io.scalajs.npm.express.fileupload.ExpressFileUpload
import io.scalajs.npm.express.{Express, Request, Response}
import io.scalajs.npm.expressws._
import io.scalajs.npm.mongodb.MongoClient

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

/**
  * Socialized Node Server
  * @author lawrence.daniels@gmail.com
  */
@JSExportAll
object SocializedJSServer extends js.JSApp {

  override def main() {
    // determine the port to listen on
    val port = process.env.get("port").map(_.toInt) getOrElse 1337
    val mongoServers = process.env.get("mongo_servers") getOrElse "localhost:27017"

    console.log("Loading Express modules...")
    implicit val app = Express().withWsRouting
    implicit val wss = ExpressWS(app)

    // setup the body parsers
    console.log("Setting up body parsers...")
    app.use(BodyParser.json())
    app.use(BodyParser.urlencoded(new UrlEncodedBodyOptions(extended = true)))

    // setup the routes for serving static files
    console.log("Setting up the routes for serving static files...")
    app.use(ExpressFileUpload())
    app.use(Express.static("public"))
    app.use("/assets", Express.static("public"))
    app.use("/bower_components", Express.static("bower_components"))

    // disable caching
    app.disable("etag")

    // setup logging of the request - response cycles
    app.use((request: Request, response: Response, next: NextFunction) => {
      val startTime = System.currentTimeMillis()
      next()
      response.onFinish(() => {
        val elapsedTime = System.currentTimeMillis() - startTime
        console.log(s"[node] application - ${request.method} ${request.originalUrl} ~> ${response.statusCode} [$elapsedTime ms]")
      })
    })

    // setup mongodb connection
    val mongoUrl = s"mongodb://$mongoServers/socialized"
    console.log("Connecting to %s", mongoUrl)
    val dbFuture = MongoClient.connectFuture(mongoUrl)

    // setup searchable entity routes
    EventRoutes.init(app, dbFuture)
    GroupRoutes.init(app, dbFuture)
    SearchRoutes.init(app, dbFuture)
    UserRoutes.init(app, dbFuture)

    // setup notification routes
    NotificationRoutes.init(app, dbFuture)
    WebSocketRoutes.init(app, wss, dbFuture)

    // setup post routes
    PostRoutes.init(app, dbFuture)

    // setup authentication/session routes
    AuthenticationRoutes.init(app, dbFuture)
    SessionRoutes.init(app, dbFuture)

    // start the listener
    app.listen(port, () => console.log(s"Server now listening on port $port..."))
    ()
  }

}