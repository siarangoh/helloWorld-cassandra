name := "cassadra-helloworld"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.outworkers"    %% "phantom-dsl"   % "2.13.4" withSources () withJavadoc (),
  "com.outworkers"    %% "phantom-jdk8"  % "2.13.4",
  "org.cassandraunit" % "cassandra-unit" % "3.1.3.2"
)