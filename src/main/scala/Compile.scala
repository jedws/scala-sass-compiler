package net.fyrie.sass

import util.parsing.input.StreamReader
import util.parsing.combinator.Parsers

object Compile {
  def main(args: Array[String]) {
    val in = args(0)
    val out = args(1)
    file(in).fold(handleInvalid, write(out, _))
  }

  def file(name: String): Either[Invalid, String] = Load(name).right.flatMap { Sass(_) }

  def write(name: String, s: String) = println("########: " + s)

  def handleInvalid(i: Invalid) = println(i)
}

object Load {
  def apply(name: String): Either[Invalid, StreamReader] = either { StreamReader(io.Source.fromFile(name).bufferedReader) }
}

