import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"

  lazy val organizeImports = "com.github.liancheng" %% "organize-imports" % "0.3.1-RC3"

  lazy val semanticdbScalac = "org.scalameta" % "semanticdb-scalac" % "4.3.18"
}
