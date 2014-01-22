name := "bitcoinrpc"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.9",
	"junit" % "junit" % "4.8" % "test",
	"org.slf4j" % "slf4j-jdk14" % "1.6.4",
	"io.argonaut" %% "argonaut" % "6.0.1",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
    "org.apache.commons" % "commons-email" % "1.2",
    "org.constretto" %% "constretto-scala" % "1.0"
)

