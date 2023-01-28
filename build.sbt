ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "zio-kafka-example"
  )

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-kafka" % "2.0.6",
  "dev.zio" %% "zio"       % "2.0.6",
  "dev.zio" %% "zio-json"  % "0.4.2",
  "com.github.pureconfig" %% "pureconfig-core" % "0.17.2"
)
