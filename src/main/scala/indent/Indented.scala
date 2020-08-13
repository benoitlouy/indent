package indent

sealed trait Element extends Product with Serializable

object Element {

  final case class String(s: scala.Predef.String) extends Element
  case object NewLine extends Element
  case object AddIndent extends Element
  case object RemoveIndent extends Element
}

final class Indented(private[indent] val content: Vector[Element]) extends AnyVal {

  def indent(implicit indent: Indentation): String = indentWith(indent.value)

  def indentWith(indentation: String): String = {
    val res = content
      .foldLeft((0, true, "", "")) {
        case ((count, false, line, acc), Element.String(str)) =>
          (count, false, s"$line$str", acc)
        case ((count, true, line, acc), Element.String(str)) =>
          (count, false, s"$line${indentation * count}$str", acc)
        case ((count, _, line, acc), Element.NewLine) if (line.matches(s"^(${indentation})+$$")) =>
          (count, true, "", s"$acc\n")
        case ((count, _, line, acc), Element.NewLine) =>
          (count, true, "", s"$acc$line\n")
        case ((count, nl, line, acc), Element.AddIndent) => (count + 1, nl, line, acc)
        case ((count, nl, line, acc), Element.RemoveIndent) => (if (count == 0) 0 else count - 1, nl, line, acc)
      }

    s"${res._4}${res._3}"
  }

  override def toString: String = indentWith("•")
}
