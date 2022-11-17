ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "producer",
    idePackagePrefix := Some("co.edu.escuelaing")
  )

val circeVersion = "0.14.1"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.4.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.7.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.7.0"