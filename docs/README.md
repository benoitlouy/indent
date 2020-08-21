# indent

[![maven](https://maven-badges.herokuapp.com/maven-central/com.github.benoitlouy/indent_2.13/badge.svg)](https://search.maven.org/artifact/com.github.benoitlouy/indent_2.13)
[![scalaci](https://github.com/benoitlouy/indent/workflows/Scala%20CI/badge.svg)](https://github.com/benoitlouy/indent/actions?query=workflow%3A%22Scala+CI%22)
[![codecov](https://codecov.io/gh/benoitlouy/indent/branch/master/graph/badge.svg)](https://codecov.io/gh/benoitlouy/indent)

Indentation aware string interpolation.

To include indent in your project

```scala
libraryDependencies += "@ORGANIZATION@" %% "@NAME@" % "@VERSION@"
```
## Usage

```scala mdoc
import indent._
import spaces2._

// create an Indented block from a String
val sectionContent = """Lorem ipsum
  |Indented list
  |  - item 1
  |  - item 2""".stripMargin.indented

// combine Indented blocks while maintaining indentation
val doc = indent"""Header
  |  1. First Section
  |    ${sectionContent}
  |  2. Second Section
  |    ${sectionContent}"""

// generate a String with 2 spaces for indentation
doc.indent

// generate a String with 4 spaces for indentation
doc.indentWith("    ")

// an indented block can also be generated from a sequence of Indented
val indentedBlocks = Vector(
  indent"""  1. First Entry
          |    Content""",
  indent"""  2. Second Entry
          |    Other Content"""
).indented

indent"""Header
  |  $indentedBlocks"""
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
