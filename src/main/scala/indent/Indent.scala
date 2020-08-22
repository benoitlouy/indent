package indent

import scala.annotation.tailrec

trait IndentInterpolatorInstances {
  object spaces2 extends Indent("  ")
  object spaces4 extends Indent("    ")
  object tab extends Indent("\t")

  implicit def toIndentedStringOps(s: String): StringOps = new StringOps(s)
  implicit def toIndentedSeqOps(s: Seq[Indented]): SeqOps = new SeqOps(s)
}

class StringOps(val s: String) extends AnyVal {
  def indented(implicit indentation: Indentation): Indented = indentedWith(indentation.value)
  def indentedWith(indentation: String): Indented = Utils.toIndented(indentation, Vector(Part.String(s)))
}

class SeqOps(val s: Seq[Indented]) extends AnyVal {
  def indented: Indented = _indented(None)
  def indented(sep: String): Indented = _indented(Some(sep))
  private def _indented(sep: Option[String]): Indented = {
    if (s.isEmpty) new Indented(Vector.empty)
    else {
      def reset(i: Indented): Vector[Element] = {
        val count = i.content.foldLeft(0) {
          case (count, Element.AddIndent) => count + 1
          case (count, Element.RemoveIndent) => count - 1
          case (count, _) => count
        }
        if (count > 0) Vector.fill(count)(Element.RemoveIndent)
        else Vector.fill(-count)(Element.AddIndent)
      }
      val es = s.tail.foldLeft(s.head.content ++ reset(s.head)) { (acc, i) =>
        acc ++ Vector(sep.map(Element.String), Some(Element.NewLine)).flatten ++ i.content ++ reset(i)
      }
      new Indented(es)
    }
  }
}

sealed trait Part extends Product with Serializable
object Part {
  final case class String(s: scala.Predef.String) extends Part
  final case class Indented(i: indent.Indented) extends Part
  final case class Other(o: Any) extends Part
}

case class Indentation(value: String)

private[indent] object Utils {
  def isLineBreak(c: Char) = c == '\n' || c == '\f'

  @tailrec
  def split(acc: Vector[String], s: String): Vector[String] = {
    val (pre, post) = s.span(c => !isLineBreak(c))
    if (post.headOption.exists(c => isLineBreak(c)))
      split(acc :+ s"$pre${post.headOption.map(_.toString).getOrElse("")}", post.drop(1))
    else
      acc :+ pre
  }

  @tailrec
  def countAndStripIndent(indent: String, count: Int, line: String): (Int, String) =
    if (line.startsWith(indent))
      countAndStripIndent(indent, count + 1, line.stripPrefix(indent))
    else
      count -> line

  def toIndented(indent: String, parts: Vector[Part]): Indented = {
    val splitParts = parts.flatMap {
      case i: Part.Indented => Vector(i)
      case Part.String(s) => split(Vector.empty, s).map(Part.String)
      case o: Part.Other => Vector(o)
    }
    val res = splitParts
      .foldLeft(Vector.empty[Element] -> 0) {
        case ((acc, lastIndentCount), Part.Other(o)) => (acc :+ Element.String(o.toString)) -> lastIndentCount
        case ((acc, lastIndentCount), Part.Indented(i)) =>
          val diff = i.content.foldLeft(0) {
            case (count, Element.AddIndent) => count + 1
            case (count, Element.RemoveIndent) => count - 1
            case (count, _) => count
          }
          val reset = if (diff > 0) Vector.fill(diff)(Element.RemoveIndent) else Vector.fill(-diff)(Element.AddIndent)
          (acc ++ i.content ++ reset) -> lastIndentCount
        case ((acc, lastIndentCount), Part.String(s)) =>
          val (indentCount, strippedLine) = countAndStripIndent(indent, 0, s)
          val diff = indentCount - lastIndentCount
          val indents = if (diff < 0) Vector.fill(-diff)(Element.RemoveIndent) else Vector.fill(diff)(Element.AddIndent)
          val addNewLine = strippedLine.lastOption.exists(isLineBreak)
          val l =
            if (addNewLine) Vector(Element.String(strippedLine.dropRight(1)), Element.NewLine)
            else Vector(Element.String(strippedLine))
          (acc ++ indents ++ l) -> indentCount
      }
      ._1
      .filter {
        case Element.String("") => false
        case _ => true
      }

    new Indented(res)
  }

}

object Indent {
  def using(indentation: String): Indent = new Indent(indentation)
}

class Indent(indent: String) { outer =>

  import Utils._

  implicit val indentation: Indentation = Indentation(indent)

  implicit class IndentInterpolator(stringContext: StringContext) {
    final def i(indents: Any*): Indented = {
      def stripTrailingPart(s: String) = {
        val (pre, post) = s.span(c => !isLineBreak(c))
        pre + post.stripMargin
      }
      val stripped: List[String] = stringContext.parts.toList match {
        case head :: tail => head.stripMargin :: (tail.map(stripTrailingPart))
        case Nil => Nil
      }

      val stringParts = stripped.map(Part.String)
      val indentParts = indents.map {
        case i: Indented => Part.Indented(i)
        case o => Part.Other(o)
      }

      val parts = stringParts
        .zip(indentParts)
        .flatMap {
          case (s, i) => Vector(s, i)
        }
        .toVector :+ Part.String(stripped.last)

      toIndented(outer.indent, parts)
    }
  }

  override def toString: String = s"Indent($indent)"
}
