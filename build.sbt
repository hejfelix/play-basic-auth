import Dependencies._


/**
  * BEGIN SONATYPE SECTION
  */

useGpg := true

val sonatypeUser     = sys.env.getOrElse("SONATYPE_USER", "NO_USER_SPECIFIED")
val sonatypePassword = sys.env.getOrElse("SONATYPE_PASSWORD", "NO_PASSWORD_SPECIFIED")
credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", sonatypeUser, sonatypePassword)

pomIncludeRepository := { _ => false }

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

homepage := Some(url("http://example.com"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/hejfelix/play-basic-auth"),
    "scm:git@github.com:hejfelix/play-basic-auth"
  )
)

developers := List(
  Developer(
    id    = "hargreaves",
    name  = "Felix Hargreaves",
    email = "hargreaves@lambdaminute.com",
    url   = url("http://lambdaminute.com")
  )
)
/**
  * END SONATYPE SECTION
  */


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
