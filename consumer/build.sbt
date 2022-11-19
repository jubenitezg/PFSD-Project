ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "consumer",
    idePackagePrefix := Some("co.edu.escuelaing")
  )

val sparkVersion = "3.2.2"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
//libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
//libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion