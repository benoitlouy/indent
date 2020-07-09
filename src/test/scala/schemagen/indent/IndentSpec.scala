package indent

import scala.language.postfixOps

import org.scalatest.freespec.AnyFreeSpecLike

class IndentSpec extends AnyFreeSpecLike {

  "format text using explicit syntax" in {
    val res = Indent.start.add(Line("foo")).add(AddIndent).add(Line("bar")).add(Line("baz")).add(RemoveIndent).add(Line("end"))

    val expected =
      """foo
        |  bar
        |  baz
        |end""".stripMargin
    assert(res.format("  ") == expected)
  }

  "format text using operators" in {
    val res = Indent.start << "foo" >| "bar" << "baz" |< "end"

    val expected =
      """foo
        |  bar
        |  baz
        |end""".stripMargin
    assert(res.format("  ") == expected)
  }

  "combine Indents" in {
    val prelude = Indent.start << "object Inner {"

    val content = Indent.start <<
      "val i: Int = 42" <<
      """val s: String = "foo"""" <<
      "def f(a: Int, b: Int): Int =" >|
      "a + b" |<

    val inner = prelude >| content |< "}"

    val wrapper = Indent.start << "object Wrapper {" >| inner |< "}"

    val expected =
      """object Wrapper {
        |  object Inner {
        |    val i: Int = 42
        |    val s: String = "foo"
        |    def f(a: Int, b: Int): Int =
        |      a + b
        |  }
        |}""".stripMargin
    assert(wrapper.format("  ") == expected)
  }

  "add multiline string" in {
    val prelude = Indent.start << "object Inner {"

    val content = Indent.start <<
      "val i: Int = 42" <<
      """val s: String = "foo"""" <<
      "def f(a: Int, b: Int): Int =" >|
      "a + b" |<

    val raw =
      """val d: Double = 3.14
        |
        |val f: Float = 6.28
        |val arr: Array[String] = Array("foo", "bar")""".stripMargin

    val inner = prelude >| content << raw |< "}"

    val wrapper = Indent.start << "object Wrapper {" >| inner |< "}"

    val expected =
      """object Wrapper {
        |  object Inner {
        |    val i: Int = 42
        |    val s: String = "foo"
        |    def f(a: Int, b: Int): Int =
        |      a + b
        |    val d: Double = 3.14
        |
        |    val f: Float = 6.28
        |    val arr: Array[String] = Array("foo", "bar")
        |  }
        |}""".stripMargin
    assert(wrapper.format("  ") == expected)

  }

}
