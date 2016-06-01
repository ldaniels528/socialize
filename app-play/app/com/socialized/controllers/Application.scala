package com.socialized.controllers

import com.socialized.actors.WebSocketHandlingActor
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller, WebSocket}

/**
  * Application Controller
  * @author lawrence.daniels@gmail.com
  */
object Application extends Controller {

  /**
    * View index.html
    */
  def index = Action {
    Ok(assets.views.html.index())
  }

  /**
    * Web Socket Hook
    */
  def webSocket = WebSocket.acceptWithActor[JsValue, JsValue] { request =>
    out => WebSocketHandlingActor.props(out)
  }

}
