package net.fyrie.sass

sealed trait Invalid
case class InvalidString(msg: String) extends Invalid
case class InvalidException(x: Throwable) extends Invalid