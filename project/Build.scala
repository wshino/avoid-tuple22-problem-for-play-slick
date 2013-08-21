import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "avoid-tuple22-problem-for-play-slick"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "com.github.tototoshi" %% "slick-joda-mapper" % "0.1.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions += "-feature"
  )

}
