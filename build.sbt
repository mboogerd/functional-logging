name := "functional-logging"

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.github.mboogerd"

lazy val root = project.in(file("."))
  .settings(ReleaseConf.publishSettings)
  .settings(GenericConf.settings())
  .settings(DependenciesConf.settings)
  .settings(DockerConf.settings)
  .settings(LicenseConf.settings)
  .enablePlugins(JavaServerAppPackaging, AutomateHeaderPlugin)