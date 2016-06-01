package com.socialized.javascript.routes

import com.github.ldaniels528.meansjs.nodejs.mongodb.{Db, MongoDB}
import com.github.ldaniels528.meansjs.nodejs.console
import com.github.ldaniels528.meansjs.nodejs.express.{Application, Request}
import com.github.ldaniels528.meansjs.nodejs.expressws.{WS, WsInstance, WsRouting}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.JSON

/**
  * WebSocket Routes
  * @author lawrence.daniels@gmail.com
  */
object WebSocketRoutes {

  def init(app: Application with WsRouting, wss: WsInstance, dbFuture: Future[Db])(implicit ec: ExecutionContext, mongo: MongoDB) = {
    // http://stackoverflow.com/questions/28899071/make-two-socket-connections-talk-to-each-other
    app.ws("/websocket", (ws: WS, req: Request) => {
      console.log("Web socket connected...")
      //console.log("request", req)
      //console.log("ws", ws)
      //console.log("wss", wss.getWss())

      ws.on("message", (message: js.Any) => {
        console.log(s"message: ${JSON.stringify(message)}")
      })
    })
  }

}
