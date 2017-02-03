import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport.{ModuleKind, scalaJSModuleKind}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt.Project.projectToRef
import sbt._

val appVersion = "0.8.25"
val scalaJsIOVersion = "0.3.0.3"

val _scalaVersion = "2.11.8"
val akkaVersion = "2.4.4"
val playVersion = "2.4.8"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked",
  "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")

val scalaJsOutputDir = Def.settingKey[File]("Directory for Javascript files output by ScalaJS")

val jsCommonSettings = Seq(
  scalaVersion := _scalaVersion,
  scalacOptions ++= Seq("-feature", "-deprecation"),
  scalacOptions in(Compile, doc) ++= Seq(
    "-no-link-warnings" // Suppresses problems with Scaladoc @throws links
  ),
  relativeSourceMaps := true,
  homepage := Some(url("https://github.com/ldaniels528/socialized")),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % _scalaVersion
  ))

lazy val shared = (project in file("app-shared"))
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .settings(jsCommonSettings: _*)
  .settings(
    name := "socialized-commonjs",
    organization := "com.github.ldaniels528",
    version := appVersion,
    libraryDependencies ++= Seq(
      "io.scalajs" %%% "core" % scalaJsIOVersion
    ))

lazy val angularjs = (project in file("app-angularjs"))
  .aggregate(shared)
  .dependsOn(shared)
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .settings(jsCommonSettings: _*)
  .settings(
    name := "socialized-angularjs",
    organization := "com.github.ldaniels528",
    version := appVersion,
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "io.scalajs" %%% "angularjs-bundle" % scalaJsIOVersion
    ))

lazy val nodejs = (project in file("app-nodejs"))
  .aggregate(angularjs)
  .dependsOn(angularjs)
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .settings(jsCommonSettings: _*)
  .settings(
    name := "socialized-nodejs",
    organization := "com.github.ldaniels528",
    version := appVersion,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    relativeSourceMaps := true,
    Seq(packageScalaJSLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
      crossTarget in(angularjs, Compile, packageJSKey) := baseDirectory.value / ".." / "public" / "javascripts"
    },
    compile in Compile <<=
      (compile in Compile) dependsOn (fastOptJS in(angularjs, Compile)),
    ivyScala := ivyScala.value map (_.copy(overrideScalaVersion = true)),
    libraryDependencies ++= Seq(
      "io.scalajs.npm" %%% "bcrypt" % scalaJsIOVersion,
      "io.scalajs.npm" %%% "express-fileupload" % scalaJsIOVersion,
      "io.scalajs.npm" %%% "express-ws" % scalaJsIOVersion,
      "io.scalajs.npm" %%% "mean-stack-bundle" % scalaJsIOVersion,
      "io.scalajs.npm" %%% "splitargs" % scalaJsIOVersion,
      "io.scalajs.npm" %%% "request" % scalaJsIOVersion
    ))

lazy val playapp = (project in file("app-play"))
  .aggregate(angularjs)
  .dependsOn(angularjs)
  .enablePlugins(PlayScala, SbtTwirl, SbtWeb)
  .settings(
    name := "socialized-play",
    organization := "com.github.ldaniels528",
    version := appVersion,
    scalaVersion := _scalaVersion,
    relativeSourceMaps := true,
    scalaJsOutputDir := (crossTarget in Compile).value / "classes" / "public" / "javascripts",
    pipelineStages := Seq(gzip, scalaJSProd),
    Seq(packageScalaJSLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
      crossTarget in(angularjs, Compile, packageJSKey) := scalaJsOutputDir.value
    },
    compile in Compile <<=
      (compile in Compile) dependsOn (fastOptJS in(angularjs, Compile)),
    ivyScala := ivyScala.value map (_.copy(overrideScalaVersion = true)),
    libraryDependencies ++= Seq(filters, json, ws,
      //
      // TypeSafe dependencies
      //
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
      "com.typesafe.play" %% "play" % playVersion,
      //
      // Third Party dependencies
      //
      "com.github.jsonld-java" % "jsonld-java" % "0.7.0",
      "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
      "org.imgscalr" % "imgscalr-lib" % "4.2",
      "org.reactivemongo" %% "play2-reactivemongo" % "0.11.11-play24",
      "org.mindrot" % "jbcrypt" % "0.3m",
      //
      // Web Jar dependencies
      //
      "org.webjars" % "angularjs" % "1.5.3",
      "org.webjars" % "angularjs-toaster" % "0.4.8",
      "org.webjars" % "angular-ui-bootstrap" % "0.14.3",
      "org.webjars" % "angular-ui-router" % "0.2.13",
      "org.webjars" % "bootstrap" % "3.3.6",
      "org.webjars" % "font-awesome" % "4.5.0",
      "org.webjars" % "jquery" % "2.2.2",
      "org.webjars" % "nervgh-angular-file-upload" % "2.1.1",
      "org.webjars" %% "webjars-play" % "2.4.0-2",
      //
      // Testing dependencies
      //
      "org.mockito" % "mockito-all" % "1.9.5" % "test",
      "org.scalatest" %% "scalatest" % "2.2.2" % "test"
    ))

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project nodejs", _: State)) compose (onLoad in Global).value
