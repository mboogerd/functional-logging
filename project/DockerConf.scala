import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging.autoImport.bashScriptExtraDefines
import com.typesafe.sbt.packager.docker.Cmd
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.Keys._
import sbt._

object DockerConf {
  // docker settings //
  lazy val settings: Seq[Setting[_]] = Seq(
    mainClass in Compile := Some("com.github.mboogerd.LogTest"),
    dockerCommands := dockerCommands.value.filterNot {
      // ExecCmd is a case class, and args is a varargs variable, so you need to bind it with @
      case Cmd("USER", args@_*) => true
      // dont filter the rest
      case cmd => false
    },

    dockerExposedPorts := Seq(8080),
    dockerUpdateLatest := true,
    dockerBaseImage := "functional-logging-base:latest",
    packageName in Docker := "log-test",
    // configure sbt-native-package to use config
    bashScriptExtraDefines ++= IO.readLines(sourceDirectory.value / "main" / "docker" / "filebeat")
  )
}