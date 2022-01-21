import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Def.{setting => dep}
import sbt._

object Libs {
  val `scala-async` = "org.scala-lang.modules" %% "scala-async" % "1.0.1"

  private val akkaVersion = "2.6.18"

  val `akka-stream`              = "com.typesafe.akka" %% "akka-stream"              % akkaVersion
  val `akka-actor-typed`         = "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion
  val `akka-actor-testkit-typed` = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion
  val `akka-stream-testkit`      = "com.typesafe.akka" %% "akka-stream-testkit"      % akkaVersion

  private val akkaHttpVersion = "10.2.7"

  val `akka-http`         = "com.typesafe.akka" %% "akka-http"         % akkaHttpVersion
  val `akka-http-testkit` = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion

  private val borerVersion = "1.7.2"
  val `borer-core`         = dep("io.bullet" %%% "borer-core" % borerVersion)
  val `borer-derivation`   = dep("io.bullet" %%% "borer-derivation" % borerVersion)
  val `borer-compat-akka`  = "io.bullet" %% "borer-compat-akka" % borerVersion

  val `akka-http-cors` = "ch.megard"         %% "akka-http-cors" % "1.1.2"
  val scalatest        = dep("org.scalatest" %%% "scalatest" % "3.2.10")
  val `selenium-3-141` = "org.scalatestplus" %% "selenium-3-141" % "3.2.10.0"
  val `scalajs-dom`    = dep("org.scala-js" %%% "scalajs-dom" % "1.2.0")

  val `tmt-typed` = dep("com.github.mushtaq.tmt-typed" %%% "tmt-typed" % "8dc3e04")

  private val rsocketVersion    = "1.1.1"
  val `rsocket-core`            = "io.rsocket" % "rsocket-core"            % rsocketVersion
  val `rsocket-transport-netty` = "io.rsocket" % "rsocket-transport-netty" % rsocketVersion
}

object Prometheus {
  val Version             = "0.14.1"
  val simpleclient        = "io.prometheus" % "simpleclient"        % Version
  val simpleclient_common = "io.prometheus" % "simpleclient_common" % Version
}
