import Dependencies._

import scala.language.postfixOps
import sys.process._

lazy val root =
  (project in file("."))
      .enablePlugins(BuildInfoPlugin)
      .settings(
        name := "serverless-url-shortener",
        organization := "com.ruchij",
        version := "0.0.1",
        scalaVersion := SCALA_VERSION,
        assemblyJarName in assembly := "serverless-url-shortener.jar",
        buildInfoKeys := BuildInfoKey.ofN(name, organization, version, scalaVersion, sbtVersion),
        buildInfoPackage := "com.ruchij.eed3si9n",
        libraryDependencies ++=
          Seq(
            awsLambdaJavaCore,
            awsLambdaJavaEvents,
            playJson,
            dynamoDbSdk
          )
      )

lazy val serverlessDeploy = taskKey[Unit]("Deploy Serverless service")

serverlessDeploy := {
  "serverless deploy -v" !
}

addCommandAlias("deploy", ";assembly ;serverlessDeploy")