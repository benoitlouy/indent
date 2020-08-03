# indent

[![scalaci](https://github.com/benoitlouy/indent/workflows/Scala%20CI/badge.svg)](https://github.com/benoitlouy/indent/actions?query=workflow%3A%22Scala+CI%22)
[![codecov](https://codecov.io/gh/benoitlouy/indent/branch/master/graph/badge.svg)](https://codecov.io/gh/benoitlouy/indent)

Indentation aware string interpolation.

To include indent in your project

```scala
libraryDependencies += "com.github.benoitlouy" %% "indent" % "@VERSION@"
```

```scala mdoc
import indent.spaces2._

// create an Indented block
val sectionContent = indent"""Lorem ipsum
  |Indented list
  |  - item 1
  |  _ item 2"""

// combining Indented blocks
val doc = indent"""Header
  |  1. First Section
  |    ${sectionContent}
  |  2. Second Section
  |    ${sectionContent}"""

println(doc.indent)
```
