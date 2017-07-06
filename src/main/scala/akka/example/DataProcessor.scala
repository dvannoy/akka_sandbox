package akka.example

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import akka.example.DataProcessor.{CreateJob, GetStatus, Status}
import util.JobType

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class DataProcessor() extends Actor with ActorLogging {
  log.info("Data Processor initiated")
  private var status = "Running"
  var jobList = Map.empty[ActorRef, (JobType, Int)]

  override def receive(): Receive = {
    case GetStatus =>
      log.info("Current status: {}", getStatus)
      sender() ! Status(getStatus)
    case CreateJob(j, l) =>
      log.info("Creating job of type {} with limit {}", j, l)
      val job = createJob(j, l)
      context.watch(job)
    case _ => sender() ! "Invalid command received"
  }


//  override val supervisorStrategy(): SupervisorStrategy = OneForOneStrategy() {
//    // TO DO: Customize supervisor strategy
//  }

  def getStatus: String = {
    return status
  }

  // method to create a new Job actor instance for each job added and keep track in the job list
  def createJob(jobType: JobType, limit: Int): ActorRef = {
    val job: ActorRef = context.actorOf(Job.props(jobType, limit))
    jobList += (job -> (jobType, limit))
    log.info("Job {} added to list", job.path.name)
    job
  }

}

object DataProcessor {

  def props(): Props = Props(new DataProcessor)

  case object GetStatus
  case class Status(status: String)
  case class CreateJob(jobType: JobType, limit: Int)
}
