val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"

lazy val root = (project in file("."))
  .settings(
    organization := "es.david",
    name := "003-scala-http4s-rock-the-jvm",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

enablePlugins(JavaAppPackaging)
dockerExposedPorts ++= Seq(8888)