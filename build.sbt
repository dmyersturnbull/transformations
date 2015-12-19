/*
   Copyright 2015 PharmGKB
             2015 Douglas Myers-Turnbull

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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

pomExtra :=
	<tmp>
		<url>https://github.com/dmyersturnbull/transformations</url>
		<licenses>
			<license>
				<name>Apache License, Version 2.0</name>
				<url>http://www.apache.org/licenses/LICENSE-2.0</url>
			</license>
		</licenses>
		<scm>
			<url>https://github.com/dmyersturnbull/transformations</url>
			<connection>https://github.com/dmyersturnbull/transformations.git</connection>
		</scm>
		<organization>
			<name>The Pharmacogenomics Knowledgebase</name>
			<url>https://www.pharmgkb.org</url>
		</organization>
		<developers>
			<developer>
				<id>dmyersturnbull</id>
				<name>Douglas Myers-Turnbull</name>
				<url>https://www.dmyersturnbull.com</url>
				<timezone>-8</timezone>
			</developer>
		</developers>
		<issueManagement>
			<system>Github</system>
			<url>https://github.com/dmyersturnbull/transformations/issues</url>
		</issueManagement>
	</tmp>
