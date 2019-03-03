import sbt._

object Dependencies {

  val SCALA_VERSION = "2.12.8"

  lazy val awsLambdaJavaEvents = "com.amazonaws" % "aws-lambda-java-events" % "2.2.5"

  lazy val awsLambdaJavaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.7.1"

  lazy val slick = "com.typesafe.slick" %% "slick" % "3.3.0"

  lazy val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"

  lazy val playSlick = "com.typesafe.play" %% "play-slick" % "4.0.0"

  lazy val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"

  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"

  lazy val mysql = "mysql" % "mysql-connector-java" % "8.0.15"

  lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.25.2"

  lazy val h2 = "com.h2database" % "h2" % "1.4.198"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10.1"

  lazy val googleGuice = "com.google.inject" % "guice" % "4.2.2"
}
