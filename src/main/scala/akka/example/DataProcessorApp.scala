package akka.example

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.example.Job.{CopyFile, PrintFile}
import akka.example.JobManager.{CreateJob, KillJob}
import akka.util.Timeout
import util.JobInstruction.{CopyFileInstruction, PrintFileInstruction}

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
  }

  @tailrec
  private def commandLoop(): Unit = {
    println("Enter instructions...")
    println("  j <id> = create new job")
    println("  jc <instruction> = send new instruction")
    println("  kill <actor_path> = kill a job")
    Command(StdIn.readLine()) match {
      case Command.Job(id) =>
        manager ! CreateJob(id)
        commandLoop
      case Command.JobCommand(job: String, instruction: JobInstruction, parameter1: String, parameter2: String) =>
        instruction match {
          case PrintFileInstruction =>
            val jobRef = system.actorSelection(job)
            jobRef ! PrintFile(parameter1)
          case CopyFileInstruction =>
            val jobRef = system.actorSelection(job)
            jobRef ! CopyFile(parameter1, parameter2)
        }
        commandLoop
      case Command.Quit =>
        system.terminate
      case Command.Kill(job: String) =>
        log.info("Requesting to kill {}", job)
        manager ! KillJob(job)
//        val jobRef = system.actorSelection("user/job-manager/" + job)
//        jobRef ! PoisonPill
//        jobRef.resolveOne().onComplete() {
//          case Success(actorRef) => actorRef ! PoisonPill
//          case Failure(ex) => log.error("Cannot kill job, user/job-manager/" + job + " does not exist")
//        }
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