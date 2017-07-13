package akka.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.example.JobManager.CreateJob
import akka.pattern.ask
import akka.util.Timeout

import scala.io.StdIn
import util.{JobInstruction, Terminal}

import scala.annotation.tailrec

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class DataProcessorApp(system: ActorSystem) extends Terminal {

  private val log = Logging(system, getClass.getName)
  implicit private val dataProcessorTimeout = Timeout(100, TimeUnit.SECONDS)

  private val manager = createManager()

  def createManager(): ActorRef =
  system.actorOf(JobManager.props(), "job-manager")

  def run(): Unit = {
    commandLoop()

    //processor ! GetStatus
  }

  @tailrec
  private def commandLoop(): Unit = {
    println("Enter instructions...")
    println("  j <id> = create new job")
    println("   jc <instruction> = send new instruction")
    Command(StdIn.readLine()) match {
      case Command.Job(id) =>
        manager ! CreateJob(id)
        commandLoop
      case Command.JobCommand(job: String, instruction: JobInstruction) =>
        manager ! "IT WORKED"
        commandLoop
      case Command.Quit =>
        system.terminate
      case Command.Unknown(command) =>
        log.warning("Unknown command {}", command)
        commandLoop
    }
  }
//  import system.dispatcher
//  protected def status(): Unit = {
//    val response = processor ? DataProcessor.GetStatus
//  }

}

object DataProcessorApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("data-processor-system")
    val dataProcessorApp = new DataProcessorApp(system)
    dataProcessorApp.run
  }
}