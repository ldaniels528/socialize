package com.socialized.javascript.directives

import com.github.ldaniels528.meansjs.angularjs.Attributes
import com.github.ldaniels528.meansjs.angularjs.{Directive, JQLite, Scope, angular}
import com.github.ldaniels528.meansjs.util.ScalaJsHelper._
import org.scalajs.dom
import com.github.ldaniels528.meansjs.core.console

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSBracketAccess, ScalaJSDefined}

/**
  * NgThumb Directive
  * @author lawrence.daniels@gmail.com
  */
class NgThumbDirective($window: Window) extends Directive[NgThumbDirectiveScope] {

  override def restrict = "A"

  override def template = "<canvas/>"

  override def link(scope: NgThumbDirectiveScope, element: JQLite, attributes: Attributes) = {
    val helper = NgThumbDirectiveHelper($window)
    if (helper.support) {
      val params = scope.$eval(attributes.dynamic.ngThumb.asInstanceOf[js.Object]).asInstanceOf[NgThumbDirectiveParams]
      if (helper.isFile(params.file) || helper.isImage(params.file)) {
        val canvas = element.find("canvas").asInstanceOf[Canvas]
        val reader = FileReader()
        reader.readAsDataURL(params.file)
        reader.onload = (event: dom.Event) => {
          val img = Image()
          img.src = event.target.dynamic.result.asInstanceOf[String]
          img.onload = () => {
            val width = params.width.getOrElse(img.width / img.height * params.height.getOrElse(1d))
            val height = params.height.getOrElse(img.height / img.width * params.width.getOrElse(1d))
            canvas.attr(js.Dynamic.literal(width = width, height = height))
            canvas(0).getContext("2d").drawImage(img, 0, 0, width, height)
          }
        }
      }
    }
  }

}

/**
  * NgThumb Directive Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait NgThumbDirectiveScope extends Scope

/**
  * NgThumb Directive Parameters
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait NgThumbDirectiveParams extends js.Object {
  var file: JSFIle = js.native
  var width: js.UndefOr[Double] = js.native
  var height: js.UndefOr[Double] = js.native
}

/**
  * NgThumb Directive Helper
  * @author lawrence.daniels@gmail.com
  */
@ScalaJSDefined
class NgThumbDirectiveHelper extends js.Object {
  var support: Boolean = _
  var isFile: js.Function1[js.UndefOr[JSFIle], Boolean] = _
  var isImage: js.Function1[js.UndefOr[JSFIle], Boolean] = _
}

/**
  * NgThumb Directive Helper Companion Object
  * @author lawrence.daniels@gmail.com
  */
object NgThumbDirectiveHelper {

  def apply($window: Window) = {
    console.log("Inside NgThumbDirectiveHelper")
    val helper = new NgThumbDirectiveHelper()
    helper.support = $window.FileReader.nonEmpty && $window.CanvasRenderingContext2D.nonEmpty
    helper.isFile = (anItem: js.UndefOr[JSFIle]) => anItem exists { item =>
      console.log(s"js.typeOf($$window.File) == ${js.typeOf($window.File)}")
      angular.isObject(item) && js.typeOf($window.File) == "File" // item instanceof $window.File;
    }
    helper.isImage = (aFile: js.UndefOr[JSFIle]) => aFile exists { file =>
      console.log(s"file.type = ${file.`type`}")
      val fileType = file.`type`.substring(file.`type`.lastIndexOf("/") + 1)
      js.Array("jpg", "png", "jpeg", "bmp", "gif").contains(fileType)
    }
    helper
  }

}

@js.native
trait Canvas extends js.Object {

  @JSBracketAccess
  def apply(index: Int): js.Dynamic = js.native

  def attr(value: js.Object): js.Dynamic = js.native

}

@js.native
trait FileReader extends js.Object {
  var onload: js.Function = js.native

  def readAsDataURL(url: js.Object): Unit = js.native

}

object FileReader {

  def apply() = New[FileReader]

}

@js.native
trait Window extends js.Object {
  var File: js.Dynamic
  var FileReader: js.UndefOr[js.Object]
  var CanvasRenderingContext2D: js.UndefOr[js.Object]
}

@ScalaJSDefined
class Image extends js.Object {
  var src: String = _
  var width: Double = _
  var height: Double = _
  var onload: js.Function = _
}

object Image {

  def apply() = {
    val image = new Image()
    image
  }

}

@js.native
trait JSFIle extends js.Object {
  val `type`: String
}