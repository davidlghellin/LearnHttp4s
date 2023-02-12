val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"

  val endpoints = "1.1.0"
  val tapir = "0.16.14"
  val circe = "0.13.0"
  val scalaTest = "3.2.0"
  val akka = "2.6.8"
  val akkaHttp = "10.2.0"


val tapirDep = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapir,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapir,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapir excludeAll ExclusionRule("com.typesafe.akka"),
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapir,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapir,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % tapir excludeAll ExclusionRule("com.typesafe.akka")
)


lazy val root = (project in file("."))
  .settings(
    organization := "es.david",
    name := "001-scala-http4s-rock-the-jvm",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,

      "org.endpoints4s" %% "algebra" % "1.9.0",
      "org.endpoints4s" %% "json-schema-generic" % "1.9.0"
    ) ++ tapirDep,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

enablePlugins(JavaAppPackaging)
dockerExposedPorts ++= Seq(8888)