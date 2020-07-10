package indent

sealed trait Element

final case class Line(str: String) extends Element
final case object EmptyLine extends Element
final case object AddIndent extends Element
final case object RemoveIndent extends Element

final class Indent private (val content: Vector[Element]) extends AnyVal {
  def add(element: Element): Indent = new Indent(content :+ element)

  def add(elements: Vector[Element]): Indent = new Indent(content ++ elements)

  def add(line: String): Indent =
    add(
      line
        .split('\n')
        .map {
          case s if s.isEmpty => EmptyLine
          case s => Line(s)
        }
        .toVector
    )

  def <<(line: String): Indent = add(line)

  def <<(o: Indent): Indent = add(o.content)

  def >|(line: String) = add(AddIndent).add(line)

  def >|(o: Indent) = add(AddIndent).add(o.content)

  def >| = add(AddIndent)

  def <|(line: String) = add(RemoveIndent).add(line)

  def <|(o: Indent) = add(RemoveIndent).add(o.content)

  def <| = add(RemoveIndent)

  def format(indent: String): String =
    content
      .foldLeft(0 -> "") {
        case ((count, acc), Line(str)) =>
          val nl = if (acc.isEmpty) "" else "\n"
          (count, s"$acc$nl${indent * count}$str")
        case ((count, acc), EmptyLine) => (count, s"$acc\n")
        case ((count, acc), AddIndent) => (count + 1, acc)
        case ((count, acc), RemoveIndent) => (if (count == 0) 0 else count - 1, acc)
      }
      ._2

  override def toString: String = format("•")

}

object Indent {
  def start: Indent = new Indent(Vector.empty)
}
