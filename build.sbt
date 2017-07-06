name := "akka_sandbox"

version := "1.0"

scalaVersion := "2.12.2"

val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.3"
)

val coreTestDeps = Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % "test"
)

val miscDeps = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
)

libraryDependencies ++= akkaDeps
libraryDependencies ++= coreTestDeps ++ miscDeps