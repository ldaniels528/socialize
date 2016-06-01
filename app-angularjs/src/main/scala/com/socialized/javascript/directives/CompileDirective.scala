package com.socialized.javascript.directives

import com.github.ldaniels528.meansjs.angularjs.{Attributes, Compile}
import com.github.ldaniels528.meansjs.angularjs.{Directive, JQLite, Scope}

import scala.scalajs.js

/**
  * Compile Directive
  * @author lawrence.daniels@gmail.com
  */
class CompileDirective($compile: Compile) extends Directive[CompileDirectiveScope] {
  override val template = """"""

  override def link(scope: CompileDirectiveScope, element: JQLite, attrs: Attributes) = {
    scope.$watch(
      { (scope: Scope) => scope.$eval(attrs.compile) }: js.Function, { (value: js.Any) =>
        // when the 'compile' expression changes assign it into the current DOM
        element.html(value)

        // compile the new DOM and link it to the current scope.
        // NOTE: we only compile .childNodes so that we don't get into infinite loop compiling ourselves
        $compile(element.contents())(scope)
        ()
      }: js.Function)
  }
}

/**
  * Compile Directive Scope
  * @author lawrence.daniels@gmail.com
  */
@js.native
trait CompileDirectiveScope extends Scope
