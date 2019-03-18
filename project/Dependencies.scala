import sbt._

object Dependencies {

  val SCALA_VERSION = "2.12.8"

  lazy val scalaReflect = "org.scala-lang" % "scala-reflect" % SCALA_VERSION

  lazy val awsLambdaJavaEvents = "com.amazonaws" % "aws-lambda-java-events" % "2.2.5"

  lazy val awsLambdaJavaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.2.0"

  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.6.13"

  lazy val slick = "com.typesafe.slick" %% "slick" % "3.2.1"

  lazy val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"

  lazy val playSlick = "com.typesafe.play" %% "play-slick" % "3.0.3"

  lazy val playSlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

  lazy val postgresql = "org.postgresql" % "postgresql" % "42.2.5"

  lazy val mysql = "mysql" % "mysql-connector-java" % "8.0.15"

  lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.25.2"

  lazy val h2 = "com.h2database" % "h2" % "1.4.198"

  lazy val playReactiveMongo = "org.reactivemongo" %% "play2-reactivemongo" % "0.16.3-play26"

  lazy val redisScala = "com.github.etaty" %% "rediscala" % "1.8.0"

  val ELASTIC4S_VERSION = "6.5.1"

  lazy val elastic4sCore =  "com.sksamuel.elastic4s" %% "elastic4s-core" % ELASTIC4S_VERSION

  lazy val elastic4sHttp = "com.sksamuel.elastic4s" %% "elastic4s-http" % ELASTIC4S_VERSION

  lazy val elastic4sPlayJson =  "com.sksamuel.elastic4s" %% "elastic4s-play-json" % ELASTIC4S_VERSION

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10.1"

  lazy val googleGuice = "com.google.inject" % "guice" % "4.2.2"

  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.3"
}
