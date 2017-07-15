package akka.example

import akka.actor.{Actor, ActorLogging, ActorRef,PoisonPill, Props, Terminated}
import akka.example.JobManager.{CreateJob, KillJob}

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class JobManager extends Actor with ActorLogging {

  var jobList = Map.empty[String, ActorRef]

  override def receive(): Receive = {
    //case JobCommand(command) => job ! command
    case CreateJob(id) =>
      log.info("Creating job with id={}", id)
      val job = createJob(id)
      context.watch(job)
      sender ! job
    case KillJob(id) =>
      log.info("Sending kill request to job {}", id)
      val jobRef = jobList(id)
      jobList -= id
      jobRef ! PoisonPill
    case Terminated(ref)=>
      log.info("Terminated job {}", ref.path.toString)
      //jobList -= ref.path.toString
    case _ => sender ! "Invalid command received"
  }

  // method to create a new Job actor instance for each job added and keep track in the job list
  private def createJob(id: String): ActorRef = {
    val job: ActorRef = context.actorOf(Job.props(id), name = id)
    jobList += (id -> job)
    job
  }

}

object JobManager {

  def props(): Props = Props[JobManager]

  case class CreateJob(id: String)
  case class JobCommand(command: AnyRef)
  case class KillJob(id: String)
}


