import sbt._

object Dependencies {
  lazy val munit = "org.scalameta" %% "munit" % "0.7.14"

  lazy val organizeImports = "com.github.liancheng" %% "organize-imports" % "0.3.1-RC3"

  lazy val semanticdbScalac = "org.scalameta" % "semanticdb-scalac" % "4.3.18"
}
