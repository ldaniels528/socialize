package com.socialized.javascript.routes

import com.socialized.javascript.StringHelper._
import io.scalajs.nodejs.console
import io.scalajs.npm.request.Request
import io.scalajs.npm.splitargs.SplitArgs
import io.scalajs.util.OptionHelper._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Shared Content Parser
  * @author lawrence.daniels@gmail.com
  */
class SharedContentParser() {

  def parse(url: String)(implicit ec: ExecutionContext): Future[Map[String, String]] = {
    for {
      (response, body) <- Request.getFuture(url).future //if response.statusCode == 200

      dataSet = body.findIndices("<head", "</head>") map {
        case (start, end) => body.substring(start, end - start)
      } match {
        case Some(text) => text.extractAll("<meta", ">")
        case None => Nil
      }

    } yield (dataSet map mapify).foldLeft(Map[String, String]()) { (dict, map) => dict ++ map }
  }

  private def mapify(line: String): Map[String, String] = {
    val mapping = Map(SplitArgs(line).toSeq flatMap (_.split("[=]", 2).toSeq match {
      case Seq(key, value) => Some(key -> value.unquote)
      case values =>
        console.error("missed: %s", values.mkString(", "))
        None
    }): _*)

    (for {
      name <- mapping.get("name") ?? mapping.get("property")
      content <- mapping.get("content")
    } yield (name, content)) match {
      case Some((key, value)) => Map(key -> value)
      case None => Map.empty
    }
  }

}
