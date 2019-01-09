import sbt._

object Dependencies {

  val SCALA_VERSION = "2.12.8"

  lazy val awsLambdaJavaEvents = "com.amazonaws" % "aws-lambda-java-events" % "2.2.4"

  lazy val awsLambdaJavaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.6.13"

  lazy val dynamoDbSdk = "software.amazon.awssdk" % "dynamodb" % "2.2.0"

  lazy val slick = "com.typesafe.slick" %% "slick" % "3.2.0"

  lazy val playSlick = "com.typesafe.play" %% "play-slick" % "3.0.3"

  lazy val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

  lazy val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0"

  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10.1"

  lazy val googleGuice = "com.google.inject" % "guice" % "4.2.2"
}
