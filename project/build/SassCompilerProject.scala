import sbt._
import de.element34.sbteclipsify._

class SassCompilerProject(info: ProjectInfo) extends DefaultProject(info) with Eclipsify
{
  override def compileOptions = Optimize :: Unchecked :: super.compileOptions.toList
  
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.7" % "test->default"
  val scalacheck = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test->default"
  
  override def repositories = Set(ScalaToolsSnapshots)
}
