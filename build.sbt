name := "mars-rover-scala"

version := "0.1"

scalaVersion := "2.13.7"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

libraryDependencies += "com.beachape" % "enumeratum_2.13" % "1.7.0"
libraryDependencies += "com.beachape" % "enumeratum-circe_2.13" % "1.7.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
