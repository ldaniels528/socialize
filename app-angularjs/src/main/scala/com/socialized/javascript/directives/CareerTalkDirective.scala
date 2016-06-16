package com.socialized.javascript.directives

import org.scalajs.angularjs.{Attributes, Compile}
import org.scalajs.angularjs.sanitize.Sce
import org.scalajs.angularjs.{Directive, JQLite, Scope}
import org.scalajs.nodejs.util.ScalaJsHelper._
import com.socialized.javascript.directives.CareerTalkDirective.Emoticons
import com.socialized.javascript.models.Emoticon

import scala.scalajs.js

/**
  * CareerTalk Directive
  * @author lawrence.daniels@gmail.com
  * @example <careertalk text="post.text"></careertalk>
  */
class CareerTalkDirective($compile: Compile, $sce: Sce) extends Directive[CareerTalkDirectiveScope] {
  override val restrict = "E"
  override val scope = js.Dynamic.literal(text = "=", callback = "&")
  override val transclude = true
  override val replace = true
  override val template = """<span compile="html"></span>"""

  override def link(scope: CareerTalkDirectiveScope, element: JQLite, attrs: Attributes) = {
    scope.$watch("text", (newValue: js.UndefOr[String], oldValue: js.UndefOr[String]) => populateScope(scope, element, newValue, oldValue))
    scope.$watch("callback", (newValue: js.UndefOr[js.Any], oldValue: js.UndefOr[Any]) => {
      //newValue.foreach(v => console.log(s"callback => $v"))
    })
  }

  private def enrichHashTags(text: String) = {
    if (text.contains('#')) {
      val sb = new StringBuilder(text)
      var lastPos = -1
      do {
        val start = sb.indexOf('#', lastPos)
        if (start != -1) {
          val end = sb.indexOf(' ', start)
          val limit = if (end != -1) end else sb.length
          val hashTag = sb.substring(start, limit)
          val tag = hashTag.tail
          val hashTagWithLink = s"""<a ng-click="callback({'tag': '$tag'})">$hashTag</a>"""

          sb.replace(start, limit, hashTagWithLink)
          lastPos = start + hashTagWithLink.length
        }
        else lastPos = -1
      } while (lastPos != -1 && lastPos < sb.length)

      sb.toString()
    }

    else text
  }

  private def enrichWithEmoticons(text: String) = {
    Emoticons.foldLeft(text) { (html, emoticon) =>
      html.replaceAllLiterally(emoticon.symbol, s"""<img src="/assets/images/smilies/${emoticon.uri}">""")
    }
  }

  private def populateScope(scope: CareerTalkDirectiveScope, element: JQLite, newValue: js.UndefOr[String], oldValue: js.UndefOr[String]) {
    scope.html = newValue.map(s => enrichHashTags(enrichWithEmoticons(s)))
    //scope.html = newValue.map(s => enrichWithEmoticons(s))
  }

}

/**
  * Post Directive Companion Object
  * @author lawrence.daniels@gmail.com
  */
object CareerTalkDirective {
  var Emoticons = js.Array(
    Emoticon(symbol = ":-@", uri = "icon_mrgreen.gif", tooltip = "Big Grin"),
    Emoticon(symbol = ":-)", uri = "icon_smile.gif", tooltip = "Smile"),
    Emoticon(symbol = ";-)", uri = "icon_wink.gif", tooltip = "Wink"),
    Emoticon(symbol = ":-D", uri = "icon_biggrin.gif", tooltip = "Big Smile"),
    Emoticon(symbol = ":->", uri = "icon_razz.gif", tooltip = "Razzed"),
    Emoticon(symbol = "B-)", uri = "icon_cool.gif", tooltip = "Cool"),
    Emoticon(symbol = "$-|", uri = "icon_rolleyes.gif", tooltip = "Roll Eyes"),
    Emoticon(symbol = "8-|", uri = "icon_eek.gif", tooltip = "Eek"),
    Emoticon(symbol = ":-/", uri = "icon_confused.gif", tooltip = "Confused"),
    Emoticon(symbol = "|-|", uri = "icon_redface.gif", tooltip = "Blush"),
    Emoticon(symbol = ":-(", uri = "icon_sad.gif", tooltip = "Sad"),
    Emoticon(symbol = ":'-(", uri = "icon_cry.gif", tooltip = "Cry"),
    Emoticon(symbol = ">:-(", uri = "icon_evil.gif", tooltip = "Enraged"),
    Emoticon(symbol = ":-|", uri = "icon_neutral.gif", tooltip = "Neutral"),
    Emoticon(symbol = ":-O", uri = "icon_surprised.gif", tooltip = "Surprised"),
    Emoticon(symbol = "(i)", uri = "icon_idea.gif", tooltip = "Idea"),
    Emoticon(symbol = "(!)", uri = "icon_exclaim.gif", tooltip = "Exclamation"),
    Emoticon(symbol = "(?)", uri = "icon_question.gif", tooltip = "Question"),
    Emoticon(symbol = "=>", uri = "icon_arrow.gif", tooltip = "Arrow"))

}

/**
  * CareerTalk Directive Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait CareerTalkDirectiveScope extends Scope {
  // input fields
  var text: js.UndefOr[String] = js.native
  var callback: js.UndefOr[js.Function] = js.native

  /// output fields
  var html: Any = js.native

}

/**
  * CareerTalk Directive Scope Companion Object
  * @author lawrence.daniels@gmail.com
  */
object CareerTalkDirectiveScope {

  def apply(text: String) = {
    val scope = New[CareerTalkDirectiveScope]
    scope.text = text
    scope
  }

}