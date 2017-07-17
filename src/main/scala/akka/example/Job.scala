package akka.example

import java.io.OutputStream

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.dispatch.sysmsg.Terminate

import scala.io.{BufferedSource, Source}
import scala.util.Try

/**
  * Created by dustin-vannoy on 7/6/17.
  */
class Job(id: String) extends Actor with ActorLogging {
  // TO DO: implement actors to handle the two command types of PrintFile and CopyFile
  // PrintFile - reads data from file system and prints to output stream
  // CopyFile - takes source path and destination path and copies data directly
  import Job._

  override def receive: Receive = {
    case PrintFile(inputPath) =>
      ???
    case CopyFile(in, out) =>
      ???
    case _ => sender() ! "Invalid command received"

  }
}

object Job {
  case class PrintFile(inputPath: String)
  case class CopyFile(inputPath: String, outputPath: String)

  def props(id: String): Props = Props(classOf[Job], id)
}