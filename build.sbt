import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.lambdaminute",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT"
    )),
  name := "play-basic-auth",
  libraryDependencies ++= Seq(
    play      % Compile,
    playTest  % Test,
    scalaTest % Test
  )
)
