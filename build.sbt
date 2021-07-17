name := "fp-rest-api"
version := "0.1"
scalaVersion := "2.13.6"

val http4sVersion  = "0.23.0-RC1"
val circeVersion   = "0.14.1"
val monocleVersion = "3.0.0-M4"
val slickVersion   = "3.3.3"
val h2Version      = "1.4.200"

libraryDependencies ++= databaseDependencies
libraryDependencies ++= http4sDependencies
libraryDependencies ++= circeDependencies
libraryDependencies ++= monocleDependencies
libraryDependencies ++= testDependencies

lazy val databaseDependencies = Seq(
  "com.typesafe.slick" %% "slick"         % slickVersion,
  "com.typesafe.slick" %% "slick-testkit" % slickVersion % Test,
  "com.h2database"      % "h2"            % h2Version // % Test
)

lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

lazy val circeDependencies = Seq(
  "org.http4s" %% "http4s-circe"         % http4sVersion,
  "io.circe"   %% "circe-generic"        % circeVersion,
  "io.circe"   %% "circe-generic-extras" % circeVersion,
  "io.circe"   %% "circe-literal"        % circeVersion
)

lazy val monocleDependencies = Seq(
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion // only for Scala 2.13
)

lazy val testDependencies = Seq(
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

//libraryDependencies += "org.typelevel" %% "cats-laws" % "2.5.0" % Test
//libraryDependencies += "org.typelevel" %% "cats-effect" % "3.0.2"
//libraryDependencies += "org.typelevel" %% "cats-core" % "2.5.0"
//libraryDependencies += "org.typelevel" %% "cats-effect-laws" % "3.0.2" % Test