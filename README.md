# indent

[![maven](https://maven-badges.herokuapp.com/maven-central/com.github.benoitlouy/indent_2.13/badge.svg)](https://search.maven.org/artifact/com.github.benoitlouy/indent_2.13)
[![scalaci](https://github.com/benoitlouy/indent/workflows/Scala%20CI/badge.svg)](https://github.com/benoitlouy/indent/actions?query=workflow%3A%22Scala+CI%22)
[![codecov](https://codecov.io/gh/benoitlouy/indent/branch/master/graph/badge.svg)](https://codecov.io/gh/benoitlouy/indent)

Indentation aware string interpolation.

To include indent in your project

```scala
libraryDependencies += "com.github.benoitlouy" %% "indent" % "0.2.0-SNAPSHOT"
```

```scala
import indent.spaces2._

// create an Indented block
val sectionContent = indent"""Lorem ipsum
  |Indented list
  |  - item 1
  |  _ item 2"""
// sectionContent: indent.Indented = Lorem ipsum
// Indented list
// •- item 1
// •_ item 2

// combining Indented blocks
val doc = indent"""Header
  |  1. First Section
  |    ${sectionContent}
  |  2. Second Section
  |    ${sectionContent}"""
// doc: indent.Indented = Header
// •1. First Section
// ••Lorem ipsum
// ••Indented list
// •••- item 1
// •••_ item 2
// •2. Second Section
// ••Lorem ipsum
// ••Indented list
// •••- item 1
// •••_ item 2

println(doc.indent)
// Header
//   1. First Section
//     Lorem ipsum
//     Indented list
//       - item 1
//       _ item 2
//   2. Second Section
//     Lorem ipsum
//     Indented list
//       - item 1
//       _ item 2
```
