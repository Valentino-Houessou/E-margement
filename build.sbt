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
  "nu.validator.htmlparser" % "htmlparser" % "1.4",
  "org.docx4j" % "xhtmlrenderer" % "1.0.0",
  "org.apache.commons" % "commons-email" % "1.1",
  "com.sun.mail" % "smtp" % "1.5.5",
  "junit" % "junit" % "4.11",
  "org.mockito" % "mockito-all" % "1.10.19"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := true