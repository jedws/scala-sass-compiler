package net.fyrie.sass

import util.parsing.input.{ Reader, StreamReader }
import util.parsing.combinator.Parsers

/** Interface to be used by external processes.
 */
object Compile {
  def main(args: Array[String]) {
    apply(args(0)).fold(handleInvalid, write(args(1), _))
  }

  def apply(fileName: String): Either[Invalid, String] =
    Load(fileName).right.flatMap { Sass(_) }

  def compile(in: String, out: String): Option[Invalid] =
    apply(in).fold(Some(_), write(out, _))

  private[sass] def write(fileName: String, s: String): Option[Invalid] =
    returning(None) { println("########: " + s) }

  private[sass] def handleInvalid(i: Invalid): Invalid =
    returning(i) { println(i) }
}

object Load {
  def apply(fileName: String): Either[Invalid, Reader[Char]] =
    either { StreamReader(io.Source.fromFile(fileName).bufferedReader) }
}

