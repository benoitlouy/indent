package indent
package syntax

import scala.language.postfixOps

trait IndentSyntax {
  implicit def toIndentStringOps(s: String): IndentStringOps = new IndentStringOps(s)
}

final class IndentStringOps(val s: String) extends AnyVal {
  def <<(line: String): Indent = Indent.start << s << line
  def <<(o: Indent): Indent = Indent.start << s << o

  def >|(line: String): Indent = Indent.start << s >| line
  def >>|(line: String): Indent = Indent.start << s >>| line
  def >>>|(line: String): Indent = Indent.start << s >>>| line
  def >>>>|(line: String): Indent = Indent.start << s >>>>| line
  def >>>>>|(line: String): Indent = Indent.start << s >>>>>| line

  def >|(o: Indent): Indent = Indent.start << s >| o
  def >>|(o: Indent): Indent = Indent.start << s >>| o
  def >>>|(o: Indent): Indent = Indent.start << s >>>| o
  def >>>>|(o: Indent): Indent = Indent.start << s >>>>| o
  def >>>>>|(o: Indent): Indent = Indent.start << s >>>>>| o

  def >| : Indent = Indent.start << s >|
  def >>| : Indent = Indent.start << s >>|
  def >>>| : Indent = Indent.start << s >>>|
  def >>>>| : Indent = Indent.start << s >>>>|
  def >>>>>| : Indent = Indent.start << s >>>>>|

  def <|(line: String): Indent = Indent.start << s <| line
  def <<|(line: String): Indent = Indent.start << s <<| line
  def <<<|(line: String): Indent = Indent.start << s <<<| line
  def <<<<|(line: String): Indent = Indent.start << s <<<<| line
  def <<<<<|(line: String): Indent = Indent.start << s <<<<<| line

  def <|(o: Indent): Indent = Indent.start << s <| o
  def <<|(o: Indent): Indent = Indent.start << s <<| o
  def <<<|(o: Indent): Indent = Indent.start << s <<<| o
  def <<<<|(o: Indent): Indent = Indent.start << s <<<<| o
  def <<<<<|(o: Indent): Indent = Indent.start << s <<<<<| o

  def <| : Indent = Indent.start << s <|
  def <<| : Indent = Indent.start << s <<|
  def <<<| : Indent = Indent.start << s <<<|
  def <<<<| : Indent = Indent.start << s <<<<|
  def <<<<<| : Indent = Indent.start << s <<<<<|

}
