import Dependencies._

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.2"

lazy val supportedScalaVersions = List(scala213, scala212)

ThisBuild / scalaVersion := scala212
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.benoitlouy"
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.3.1-RC3"

inThisBuild(
  List(
    addCompilerPlugin(("org.scalameta" % "semanticdb-scalac" % "4.3.18").cross(CrossVersion.full)),
    addCompilerPlugin(("org.typelevel" %% "kind-projector" % "0.11.0").cross(CrossVersion.full))
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "indent",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
