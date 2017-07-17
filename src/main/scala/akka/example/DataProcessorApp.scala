package akka.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.example.Job.{CopyFile, PrintFile}
import akka.util.Timeout


import scala.util.Random

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class DataProcessorApp(system: ActorSystem) {

  private val log = Logging(system, getClass.getName)
  implicit private val dataProcessorTimeout = Timeout(100, TimeUnit.SECONDS)


  def run(): Unit = {
    val j = createJob(Random.alphanumeric.take(12).mkString)
    j ! PrintFile("/opt/data/sample_file.csv")
  }


  private def createJob(id: String): ActorRef = {
    val job: ActorRef = system.actorOf(Job.props(id), name = id)
    job
  }

}

object DataProcessorApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("data-processor-system")
    val dataProcessorApp = new DataProcessorApp(system)
    dataProcessorApp.run
  }
}