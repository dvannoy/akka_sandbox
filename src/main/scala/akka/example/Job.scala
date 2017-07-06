package akka.example

import akka.actor.{Actor, ActorLogging, Props}
import util.JobType

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class Job(jobType: JobType, limit: Int) extends Actor with ActorLogging {
  // TO DO: log that job was created, add cases and types to receive to make it kick off the
  //  extract, transform, and load steps by calling out to other actors

  override def receive: Receive = {
    case _ => sender() ! "Invalid command received"

  }

}

object Job {

  def props(jobType: JobType, limit: Int): Props = Props(new Job(jobType, limit))
}