import Dependencies._
import ReleaseTransformations._

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.19"
lazy val scala213 = "2.13.13"
lazy val scala3 = "3.3.3"

lazy val supportedScalaVersions = List(scala3, scala213, scala212, scala211)

ThisBuild / scalaVersion := scala213
ThisBuild / organization := "com.github.benoitlouy"
ThisBuild / organizationName := "benoitlouy"
ThisBuild / scalafixDependencies += organizeImports

lazy val root = (project in file("."))
  .settings(
    name := "indent",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies += munit % Test
  )

lazy val docs = (project in file("indent-docs"))
  .settings(
    publish / skip := true,
    mdocOut := (ThisBuild / baseDirectory).value,
    mdocVariables := Map(
      "ORGANIZATION" -> organization.value,
      "NAME" -> (root / name).value,
      "VERSION" -> version.value
    )
  )
  .dependsOn(root)
  .enablePlugins(MdocPlugin)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/benoitlouy/indent"),
    "scm:git@github.com:benoitlouy/indent.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "benoitlouy",
    name = "Benoit Louy",
    email = "benoit.louy@fastmail.com",
    url = url("https://github.com/benoitlouy")
  )
)

ThisBuild / description := "Indentation aware string interpolation"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/benoitlouy/indent"))

// Remove all additional repository other than Maven Central from POM
// ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots".at(nexus + "content/repositories/snapshots"))
  else Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
}
ThisBuild / publishMavenStyle := true
ThisBuild / Test / publishArtifact := false

releaseTagName := s"${version.value}"
releaseVcsSign := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  releaseStepInputTask(docs / mdoc),
  releaseStepCommand("git add README.md"),
  releaseStepCommand("""git commit -m "update README""""),
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)

addCommandAlias("fix", ";compile:scalafix ;test:scalafix")
addCommandAlias("cov", ";clean;coverage;+test;coverageReport")
addCommandAlias("fmt", ";scalafmtAll;scalafmtSbt")
addCommandAlias("fixCheck", ";compile:scalafix --check ;test:scalafix --check")
addCommandAlias("fmtCheck", ";scalafmtCheckAll;scalafmtSbtCheck")
addCommandAlias("check", ";cov;fixCheck;fmtCheck;docs/mdoc")
