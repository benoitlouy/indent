package indent

class IndentSpec extends munit.FunSuite {

  test("toString") {
    import indent.spaces2._

    val data = i"""Header
    |  1. Section
    |    a. Subsection
    |  2. Section"""

    assert(data.toString == data.indentWith("•"))
  }

  test("interpolation of variable of type other than Indent") {
    import indent.spaces2._

    val header = "Header"
    val i = 1
    val data = i"""$header
    |  $i. Section
    |    a. Subsection
    |  2. Section"""

    val expected = s"""Header
    |  1. Section
    |    a. Subsection
    |  2. Section""".stripMargin

    assert(data.indent == expected)
  }

  test("composition") {
    import indent.spaces2._
    val section = i"""1. Section
    |  a. Subsection"""

    val data = i"""Header
    |  $section
    |  2. Section
    |
    |"""

    val expected = s"""Header
    |  1. Section
    |    a. Subsection
    |  2. Section
    |
    |""".stripMargin

    assert(data.indent == expected)
  }

  test("nested") {
    import indent.spaces2._

    val f = i"""def f(a: Int, b: Int): Int =
    |  a + b"""

    val inner = i"""object Inner {
    |  val i: Int = 42
    |  val s: String = "foo"
    |  $f
    |  val d: Double = 3.14
    |
    |  val f: Float = 6.28
    |  val arr: Array[String] = Array("foo", "bar")
    |}"""

    val outer = i"""object Wrapper {
      |  ${inner}
      |}"""

    val expected = """object Wrapper {
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

    assert(outer.indentWith("  ") == expected)
  }

  test("recursive") {
    import indent.spaces2._

    def rec(depth: Int): Indented =
      if (depth == 0)
        i""
      else
        i"""Rec {
           |  ${rec(depth - 1)}
           |}"""

    val result = rec(5)

    val expected = """Rec {
      |  Rec {
      |    Rec {
      |      Rec {
      |        Rec {
      |        }
      |      }
      |    }
      |  }
      |}""".stripMargin
    assert(result.indent == expected)
  }

  test("String.indented creates Indented instance from string") {
    import indent.spaces2._

    val s = """Foo
    |  test
    |Bar
    |    test2""".stripMargin.indented

    val result = i"""  $s"""

    val expected = """  Foo
    |    test
    |  Bar
    |      test2""".stripMargin

    assert(result.indent == expected)
  }

  test("custom indentation") {
    val spaces3 = Indent.using("   ")
    import spaces3._

    assert("   test".indented.indentWith("") == "test")
  }

  test("Seq.indented") {
    import indent.spaces2._

    def gen(i: Int): Indented = s"${Vector.fill(i)("  ").mkString}$i. Entry".indented
    def genReversed(i: Int, total: Int): Indented = s"${Vector.fill(total - i)("  ").mkString}$i. Entry".indented

    val a = i"""Test
    |${(1 to 4).map(gen).indented}
    |  Done"""

    val expectedA = """Test
    |  1. Entry
    |    2. Entry
    |      3. Entry
    |        4. Entry
    |  Done""".stripMargin

    assert(a.indent == expectedA)

    val b = i"""Test
    |${(1 to 4).map(genReversed(_, 4)).indented}
    |  Done"""

    val expectedB = """Test
    |      1. Entry
    |    2. Entry
    |  3. Entry
    |4. Entry
    |  Done""".stripMargin

    assert(b.indent == expectedB)

    val c = i"""Test
    |  ${Seq(
      "  1. Entry\nfoo".indented,
      "  2. Entry\nbar".indented
    ).indented}
    |Done"""

    val expectedC = """Test
    |    1. Entry
    |  foo
    |    2. Entry
    |  bar
    |Done""".stripMargin

    assert(c.indent == expectedC)

    val d = i"""Vector(
    |  ${Vector(i"a", i"b").indentedWith(before = ",")}
    |)"""

    val expectedD = """Vector(
      |  a,
      |  b
      |)""".stripMargin

    assert(d.indent == expectedD)

    val e = i"""Test ${Vector(i"  1", i"  2").indentedWith(after = "with ")}
    |Done"""

    val expectedE = """Test 1
    |  with 2
    |Done""".stripMargin

    assert(e.indent == expectedE)
  }

  test("empty Seq.indented") {
    import indent.spaces2._
    val a = i"""{
    |  ${Vector.empty[Indented].indented}
    |  ${Vector.empty[Indented].indented}a
    |  b${Vector.empty[Indented].indented}
    |}
    |${Vector.empty[Indented].indented}"""

    val expected = """{
      |  a
      |  b
      |}""".stripMargin

    assert(a.indent == expected)
  }

  test("isEmpty nonEmpty") {
    import indent.spaces2._

    assert(i"".isEmpty)
    assert(Vector.empty[Indented].isEmpty)
    assert(i"Lorem ipsum".nonEmpty)
  }

  test("Seq.indentedWith") {
    import indent.spaces2._

    val expected = """a,
    |
    |b,
    |
    |c""".stripMargin

    val res = Vector(i"a", i"b", i"c").indentedWith(before = ",", newlines = 2)

    assert(res.indent == expected)
  }
}
