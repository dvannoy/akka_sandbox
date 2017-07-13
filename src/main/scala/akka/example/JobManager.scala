package akka.example

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import akka.example.JobManager.CreateJob

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class JobManager extends Actor with ActorLogging {
  log.info("Data Processor initiated")
  var jobList = Map.empty[ActorRef, String]

  override def receive(): Receive = {
    case CreateJob(id) =>
      log.info("Creating job with id={}", id)
      val job = createJob(id)
      context.watch(job)
      sender ! job
    //case JobCommand(command) => job ! command
    case s: String =>
      log.info(s)
    case Terminated(ref)=>
      jobList -= ref
    case _ => sender ! "Invalid command received"
  }

  // method to create a new Job actor instance for each job added and keep track in the job list
  private def createJob(id: String): ActorRef = {
    val job: ActorRef = context.actorOf(Job.props(id))
    jobList += (job -> (id))
    log.info("Job {} added to list", job.path.name)
    job
  }

}

object JobManager {

  def props(): Props = Props[JobManager]

  case class CreateJob(id: String)
  case class JobCommand(command: AnyRef)
}


