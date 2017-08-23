import sbt._

object Dependencies {
  val playVersion    = "2.6.3"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val play      = "com.typesafe.play" %% "play" % playVersion
  lazy val playTest  = "com.typesafe.play" %% "play-test" % playVersion
}
