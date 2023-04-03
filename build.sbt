//ThisBuild / scalaVersion := "2.13.3"
//ThisBuild / organization := "es.david"
//ThisBuild / version := "0.0.1-SNAPSHOT"
//ThisBuild / fork := true

val Http4sVersion = "0.23.18"
val CirceVersion = "0.14.3"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.11"
val MunitCatsEffectVersion = "1.0.7"

val CatsVersion = "2.2.0"
val CatsEffectVersion = "2.2.0"
val CatsTaglessVersion = "0.11"

val commonSettings =
  Seq(
    organization := "es.david",
    addCompilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.11.1" cross CrossVersion.full
    ),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % MunitVersion % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )

lazy val scalaHttp4s000 = (project in file("000-scala-htt4s"))
  .settings(commonSettings)
  .settings(
    name := "000-scala-htt4s",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime,
      "org.scalameta" %% "svm-subs" % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework"),
    scalacOptions --= Seq(
      "-Xfatal-warnings"
    )
  )
//enablePlugins(JavaAppPackaging)

val Http4sVersion2 = "1.0.0-M21"
val CirceVersion2 = "0.14.0-M5"

lazy val scalaHttp4s001 = (project in file("001-scala-http4s-rock-the-jvm"))
  .settings(commonSettings)
  .settings(
    name := "001-scala-http4s-rock-the-jvm",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion2,
      "org.http4s" %% "http4s-circe" % Http4sVersion2,
      "org.http4s" %% "http4s-dsl" % Http4sVersion2,
      "io.circe" %% "circe-generic" % CirceVersion2,

      "org.endpoints4s" %% "algebra" % "1.9.0",
      "org.endpoints4s" %% "json-schema-generic" % "1.9.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework"),
    scalacOptions --= Seq(
      "-Xfatal-warnings"
    )
  )
