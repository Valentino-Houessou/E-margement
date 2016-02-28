name := """src"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.google.code.gson" % "gson" % "2.5",
  "it.innove" % "play2-pdf" % "1.4.0",
  "commons-io" % "commons-io" % "2.4",
  "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.8",
  "nu.validator.htmlparser" % "htmlparser" % "1.4"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
