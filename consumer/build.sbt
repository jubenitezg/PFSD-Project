ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "consumer",
    idePackagePrefix := Some("co.edu.escuelaing")
  )

val sparkVersion = "3.2.2"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
libraryDependencies += "javax.mail" % "mail" % "1.4.7"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.4"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
)

