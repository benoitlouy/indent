package indent

sealed trait Element extends Product with Serializable

object Element {

  final case class String(s: scala.Predef.String) extends Element
  case object NewLine extends Element
  case object AddIndent extends Element
  case object RemoveIndent extends Element
  case object RemoveLineIfEmpty extends Element
}

final class Indented(private[indent] val content: Vector[Element]) extends AnyVal {

  def indent(implicit indent: Indentation): String = indentWith(indent.value)

  def indentWith(indentation: String): String = {
    val (curCount, _, _, line, acc, empty) = content
    // current line indent level
    // indent level count
    // follow insertion of a new line
    // line being built
    // previous lines
    // line should not be inserted if true
      .foldLeft((0, 0, true, "", "", false)) {
        case ((curCount, count, _, line, acc, _), Element.RemoveLineIfEmpty) => (curCount, count, false, line, acc, true)

        case ((curCount, count, _, line, acc, _), Element.String(str)) =>
          (curCount, count, false, s"$line$str", acc, false)

        case ((_, count, _, "", acc, empty), Element.NewLine) =>
          (count, count, true, "", if (empty) acc else s"$acc\n", false)

        case ((curCount, count, _, line, acc, _), Element.NewLine) =>
          (count, count, true, "", s"$acc${indentation * curCount}$line\n", false)

        case ((curCount, count, true, line, acc, empty), Element.AddIndent) =>
          (curCount + 1, count + 1, true, line, acc, empty)

        case ((curCount, count, false, line, acc, empty), Element.AddIndent) => (curCount, count + 1, false, line, acc, empty)

        case ((curCount, count, true, line, acc, empty), Element.RemoveIndent) => (curCount - 1, if (count == 0) 0 else count - 1, true, line, acc, empty)

        case ((curCount, count, false, line, acc, empty), Element.RemoveIndent) => (curCount, if (count == 0) 0 else count - 1, false, line, acc, empty)
      }
    val lastLine = if (empty && line.isEmpty) "" else s"${indentation * curCount}$line"
    val previousLines = if (empty && line.isEmpty && acc.lastOption.exists(_ == '\n')) acc.dropRight(1) else acc
    s"$previousLines$lastLine"
  }

  override def toString: String = indentWith("•")
}
