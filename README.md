# indent

[![maven](https://maven-badges.herokuapp.com/maven-central/com.github.benoitlouy/indent_2.13/badge.svg)](https://search.maven.org/artifact/com.github.benoitlouy/indent_2.13)
[![scalaci](https://github.com/benoitlouy/indent/workflows/Scala%20CI/badge.svg)](https://github.com/benoitlouy/indent/actions?query=workflow%3A%22Scala+CI%22)
[![codecov](https://codecov.io/gh/benoitlouy/indent/branch/master/graph/badge.svg)](https://codecov.io/gh/benoitlouy/indent)

Indentation aware string interpolation.

To include indent in your project

```scala
libraryDependencies += "com.github.benoitlouy" %% "indent" % "0.5.0"
```
## Usage

```scala
import indent._
import spaces2._

// create an Indented block from a String
val sectionContent = """Lorem ipsum
                       |Indented list
                       |  - item 1
                       |  - item 2""".stripMargin.indented
// sectionContent: Indented = Lorem ipsum
// Indented list
// •- item 1
// •- item 2

// combine Indented blocks while maintaining indentation
val doc = i"""Header
             |  1. First Section
             |    ${sectionContent}
             |  2. Second Section
             |    ${sectionContent}"""
// doc: Indented = Header
// •1. First Section
// ••Lorem ipsum
// ••Indented list
// •••- item 1
// •••- item 2
// •2. Second Section
// ••Lorem ipsum
// ••Indented list
// •••- item 1
// •••- item 2

// generate a String with 2 spaces for indentation
doc.indent
// res0: String = """Header
//   1. First Section
//     Lorem ipsum
//     Indented list
//       - item 1
//       - item 2
//   2. Second Section
//     Lorem ipsum
//     Indented list
//       - item 1
//       - item 2"""

// generate a String with 4 spaces for indentation
doc.indentWith("    ")
// res1: String = """Header
//     1. First Section
//         Lorem ipsum
//         Indented list
//             - item 1
//             - item 2
//     2. Second Section
//         Lorem ipsum
//         Indented list
//             - item 1
//             - item 2"""

// an indented block can also be generated from a sequence of Indented
val indentedBlocks = Vector(
  i"""  1. First Entry
     |    Content""",
  i"""  2. Second Entry
      |    Other Content"""
).indented
// indentedBlocks: Indented = •1. First Entry
// ••Content
// •2. Second Entry
// ••Other Content

i"""Header
   |  $indentedBlocks"""
// res2: Indented = Header
// ••1. First Entry
// •••Content
// ••2. Second Entry
// •••Other Content
```

The library provides instances for tab, 2 and 4 space indentation.

4-space indentation
```scala
import indent._
import spaces4._
```

tab indentation
```scala
import indent._
import tab._
```

Using a custom String for indentation can be achieved by creating an instance of `Indent` and importing it's members.

```scala
import indent._

val spaces3 = Indent.using("   ")
import spaces3._
```
