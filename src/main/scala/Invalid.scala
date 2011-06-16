package net.fyrie.sass

sealed trait Invalid
case class CompileError(msg: String) extends Invalid
case class ExceptionCaught(x: Throwable) extends Invalid