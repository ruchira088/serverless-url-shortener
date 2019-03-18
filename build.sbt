import Dependencies._

import scala.language.postfixOps
import scala.sys.process._

lazy val macroUtilities =
    (project in file("./macro-utils"))
      .settings(
        name := "macro-utilities",
        organization := "com.ruchij",
        version := "0.0.1",
        scalaVersion := SCALA_VERSION,
        libraryDependencies ++= Seq(typesafeConfig, scalaReflect)
      )

lazy val root =
    (project in file("."))
      .enablePlugins(BuildInfoPlugin)
      .dependsOn(macroUtilities)
      .settings(
        name := "serverless-url-shortener",
        organization := "com.ruchij",
        version := "0.0.1",
        scalaVersion := SCALA_VERSION,
        assemblyJarName in assembly := "serverless-url-shortener.jar",
        assemblyMergeStrategy in assembly := {
          case PathList("META-INF", _*) => MergeStrategy.discard
          case _ => MergeStrategy.first
        },
        buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
        buildInfoPackage := "com.ruchij.eed3si9n",
        libraryDependencies ++=
          Seq(
            awsLambdaJavaCore,
            awsLambdaJavaEvents,
            playJson,
            slick,
            slickHikaricp,
            mysql,
            playReactiveMongo,
            redisScala,
            typesafeConfig,
            jodaTime,
            googleGuice,
            postgresql,
            mysql,
            sqlite,
            h2,
            elastic4sCore, elastic4sHttp, elastic4sPlayJson
          )
      )

lazy val playServer =
    (project in file("./play-server"))
      .enablePlugins(PlayScala, BuildInfoPlugin)
      .settings(
        name := "play-url-shortener",
        organization := "com.ruchij",
        maintainer := "ruchira088@gmail.com",
        version := "0.0.1",
        scalaVersion := SCALA_VERSION,
        buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
        buildInfoPackage := "com.ruchij.eed3si9n.play",
        libraryDependencies ++=
          Seq(guice, ws, playSlick, playSlickEvolutions, playReactiveMongo)
      )
      .dependsOn(root)

lazy val serverlessDeploy = taskKey[Unit]("Deploy Serverless service")

serverlessDeploy := {
    "serverless deploy -v" !
  }

addCommandAlias("deploy", ";assembly ;serverlessDeploy")

addCommandAlias("runWithMySQL", "playServer/run -Dconfig.file=play-server/conf/application.mysql.conf")

addCommandAlias("runWithPostgres", "playServer/run -Dconfig.file=play-server/conf/application.postgres.conf")

addCommandAlias("runWithSQLite", "playServer/run")

addCommandAlias("runWithH2", "playServer/run -Dconfig.file=play-server/conf/application.h2.conf")

addCommandAlias("runWithMongo", "playServer/run -Dconfig.file=play-server/conf/application.mongo.conf")

addCommandAlias("runWithRedis", "playServer/run -Dconfig.file=play-server/conf/application.redis.conf")

addCommandAlias("runWithES", "playServer/run -Dconfig.file=play-server/conf/application.elasticsearch.conf")
