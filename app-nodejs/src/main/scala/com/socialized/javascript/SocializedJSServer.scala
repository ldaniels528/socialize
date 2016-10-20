package com.socialized.javascript

import com.socialized.javascript.routes._
import org.scalajs.nodejs._
import org.scalajs.nodejs.bodyparser._
import org.scalajs.nodejs.express.fileupload.ExpressFileUpload
import org.scalajs.nodejs.express.{Express, Request, Response}
import org.scalajs.nodejs.expressws.{ExpressWS, WsRouterExtensions}
import org.scalajs.nodejs.globals.process
import org.scalajs.nodejs.mongodb.MongoDB

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

/**
  * Socialized Node Server
  * @author lawrence.daniels@gmail.com
  */
@JSExportAll
object SocializedJSServer extends js.JSApp {

  override def main() = {}

  def startServer(implicit bootstrap: Bootstrap) {
    implicit val require = bootstrap.require

    // determine the port to listen on
    val port = process.env.get("port").map(_.toInt) getOrElse 1337
    val mongoServers = process.env.get("mongo_servers") getOrElse "localhost:27017"

    console.log("Loading Express modules...")
    implicit val express = require[Express]("express")
    implicit val app = express().withWsRouting
    implicit val wss = require[ExpressWS]("express-ws")(app)
    implicit val fileUpload = require[ExpressFileUpload]("express-fileupload")

    console.log("Loading MongoDB module...")
    implicit val mongodb = require[MongoDB]("mongodb")

    // setup the body parsers
    console.log("Setting up body parsers...")
    val bodyParser = require[BodyParser]("body-parser")
    app.use(bodyParser.json())
    app.use(bodyParser.urlencoded(new UrlEncodedBodyOptions(extended = true)))

    // setup the routes for serving static files
    console.log("Setting up the routes for serving static files...")
    app.use(fileUpload())
    app.use(express.static("public"))
    app.use("/assets", express.static("public"))
    app.use("/bower_components", express.static("bower_components"))

    // disable caching
    app.disable("etag")

    // setup logging of the request - response cycles
    app.use((request: Request, response: Response, next: NextFunction) => {
      val startTime = System.currentTimeMillis()
      next()
      response.onFinish(() => {
        val elapsedTime = System.currentTimeMillis() - startTime
        console.log("[node] application - %s %s ~> %d [%d ms]", request.method, request.originalUrl, response.statusCode, elapsedTime)
      })
    })

    // setup mongodb connection
    val mongoUrl = s"mongodb://$mongoServers/socialized"
    console.log("Connecting to %s", mongoUrl)
    val dbFuture = mongodb.MongoClient.connectFuture(mongoUrl)

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
    app.listen(port, () => console.log("Server now listening on port %d", port))
    ()
  }

}