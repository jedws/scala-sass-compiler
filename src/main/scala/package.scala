package net.fyrie

package object sass {
  def either[A](a: => A) =
    try Right(a)
    catch { case t: Exception => Left(ExceptionCaught(t)) }

  /** Syntactic sugar for executing side-effects and then returning some value.
   *  known to lispers as `prog1`
   */
  def returning[A](result: A)(f: => Unit) = { f; result }
}
