import java.io.File
import sbt.Process._
import java.lang.ProcessBuilder
import sbt._

class StreamletProject(info: ProjectInfo) extends DefaultProject(info) with IdeaProject {
  val scalaToolsReleases = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-releases"

  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test" withSources
  val bryanjswift = "Bryan J Swift Repository" at "http://repos.bryanjswift.com/maven2/"
  val junitInterface = "com.novocode" % "junit-interface" % "0.4.0" % "test"
  val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.1.1" withSources
  val junit = "junit" % "junit" % "4.8.2" % "test"
  val mockitoCore = "org.mockito" % "mockito-core" % "1.8.5" % "test"


  val streamAppName = "mystreamapp"
  override def artifactID = streamAppName
  val streamServerLocation = "/usr/local/WowzaMediaServer/"
  val streamAppConfLocation = "src/main/streamapp/"
  val confFolder = "conf/"
  val jarDeployLocation = "lib/"

  lazy val deploy = localDeploy dependsOn(`package`) describedAs("deploy jar to local mediaserver")
  def localDeploy = task {
    log.info ("Deploying application to local mediaserver")
    //Create application folder for Wowza
    FileUtilities.createDirectory(new File(streamServerLocation + "applications/" + streamAppName), log)
    
    //copy common configuration files
    new File (streamAppConfLocation + confFolder +"local/").
      listFiles.filter(file => !file.isDirectory() && !file.getName.startsWith(".svn")).foreach(fileToCopy =>
      FileUtilities.copyFile(fileToCopy, new File(streamServerLocation + confFolder + "/" + fileToCopy.getName), log))
    
    List(streamAppName).foreach {appName =>
      //Create configuration folder for Wowza using the environment local
      val targetFolder = new File(streamServerLocation + confFolder + appName)
      if (targetFolder.exists) targetFolder.listFiles.foreach(_.delete)
      targetFolder.delete
      FileUtilities.createDirectory(targetFolder, log)
      new File (streamAppConfLocation + confFolder +"local/" + appName).
        listFiles.filter(!_.getName.startsWith(".svn")).foreach(fileToCopy =>
        FileUtilities.copyFile(fileToCopy, new File(targetFolder + "/" + fileToCopy.getName), log))
    }

    new File(managedDependencyPath + "/compile").listFiles.foreach(file =>
      FileUtilities.copyFile(file, new File(streamServerLocation + jarDeployLocation + file.getName), log))
    FileUtilities.copyFile(new File("project/boot/scala-2.8.1/lib/scala-library.jar"), new File(streamServerLocation + jarDeployLocation + "/scala-library-2.8.1.jar"), log)
    //Copy jar containing module files
    FileUtilities.copyFile(jarPath.asFile, new File(streamServerLocation + jarDeployLocation + jarPath.asFile.getName), log)
  }
  
  lazy val runWowza = startWowza dependsOn(deploy) describedAs ("runs wowza but deploys the app first")
  def startWowza = task {
    (((new ProcessBuilder("sh", "startup.sh", "&")) directory new File(streamServerLocation + "bin/")) ! log)
    None
  }

    lazy val stopWowza = shutdownWowza dependsOn(deploy) describedAs ("runs wowza but deploys the app first")
  def shutdownWowza = task {
    (((new ProcessBuilder("./shutdown.sh")) directory new File(streamServerLocation + "bin/")) ! log)
    None
  }
    
}

	


