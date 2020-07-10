# indent

[![scalaci](https://github.com/benoitlouy/indent/workflows/Scala%20CI/badge.svg)](https://github.com/benoitlouy/indent/actions?query=workflow%3A%22Scala+CI%22)
[![codecov](https://codecov.io/gh/benoitlouy/indent/branch/master/graph/badge.svg)](https://codecov.io/gh/benoitlouy/indent)

To include indent in your project

```scalac
libraryDependencies += "com.github.benoitlouy" %% "indent" % "0.1.0-SNAPSHOT"
```

```scala
import scala.language.postfixOps
import indent._

val sectionContent = Indent.start <<
  "Lorem ipsum" <<
  "Indented list" >|
  "- item 1" <<
  "- item 2" <|
// sectionContent: Indent = Lorem ipsum
// Indented list
// •- item 1
// •- item 2

// combining indent instances
val prelude = Indent.start << "Header" << "1. First Section" >| sectionContent <| "2. Second Section" >| sectionContent <|
// prelude: Indent = Header
// 1. First Section
// •Lorem ipsum
// •Indented list
// ••- item 1
// ••- item 2
// 2. Second Section
// •Lorem ipsum
// •Indented list
// ••- item 1
// ••- item 2

println(prelude.format("  "))
// Header
// 1. First Section
//   Lorem ipsum
//   Indented list
//     - item 1
//     - item 2
// 2. Second Section
//   Lorem ipsum
//   Indented list
//     - item 1
//     - item 2
```
