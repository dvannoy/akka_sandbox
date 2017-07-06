package akka.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.example.DataProcessor.CreateJob
import akka.pattern.ask
import akka.util.Timeout

import scala.io.StdIn
import util.Terminal

import scala.annotation.tailrec

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class DataProcessorApp(system: ActorSystem) extends Terminal {

  private val log = Logging(system, getClass.getName)
  implicit private val dataProcessorTimeout = Timeout(100, TimeUnit.SECONDS)

  private val processor = createProcessor()

  def createProcessor(): ActorRef =
  system.actorOf(DataProcessor.props(), "data-processor")

  def run(): Unit = {
    commandLoop()

    //processor ! GetStatus
  }

  @tailrec
  private def commandLoop(): Unit =
    Command(StdIn.readLine()) match {
      case Command.Job(count, jobType, limit) =>
        (1 to count).foreach { _ => processor ! CreateJob(jobType, limit) }
        commandLoop
      case Command.Status =>
        status()
        commandLoop
      case Command.Quit =>
        system.terminate
      case Command.Unknown(command) =>
        log.warning("Unknown command {}", command)
        commandLoop
    }

  import system.dispatcher
  protected def status(): Unit = {
    val response = processor ? DataProcessor.GetStatus
  }

}

object DataProcessorApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("data-processor-system")
    val dataProcessorApp = new DataProcessorApp(system)
    dataProcessorApp.run
  }
}