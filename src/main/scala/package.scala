package net.fyrie

package object sass {
  def either[A](a: => A) =
    try Right(a)
    catch { case t: Exception => Left(InvalidException(t)) }
}
