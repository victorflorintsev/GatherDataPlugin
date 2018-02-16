organization := "ch.usi.inf.reveal.parsing"

name := "stormed-client"

version := "2.0.0"
scalaVersion := "2.12.2"

resolvers += "Sonatype Nexus Repository Manager" 	at 	"https://stormed.inf.usi.ch/releases/"

credentials += Credentials("Sonatype Nexus Repository Manager", "stormed.inf.usi.ch", "anonymous", "anonymous")

libraryDependencies ++= List(
	"ch.usi.inf.reveal.parsing"	%%	"stormed-devkit"	%	"2.0.0",
	"org.scalaj"	%%	"scalaj-http" % "2.3.0"
	
)