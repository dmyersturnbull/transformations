organization := "com.github.dmyersturnbull"

name := "transformations"

version := "0.1"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:all")

resolvers ++= Seq(
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

assemblyJarName in assembly := name.value + "-" + version.value + ".jar"


libraryDependencies ++= Seq(
	"junit" % "junit" % "4.12" % "test",
	"commons-cli" % "commons-cli" % "1.3.1",
	"org.slf4j" % "slf4j-api" % "1.7.12",
	"org.mockito" % "mockito-core" % "2.0.31-beta" % "test",
	"com.google.guava" % "guava" % "18.0",
	"org.slf4j" % "slf4j-api" % "1.7.12",
	"com.google.code.findbugs" % "jsr305" % "3.0.0",
	"javax.validation" % "validation-api" % "1.1.0.Final"
)
