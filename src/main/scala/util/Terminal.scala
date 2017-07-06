package util

import scala.util.parsing.combinator.RegexParsers

/**
  * Created by dustin-vannoy on 7/6/17.
  *   Based on Terminal trait used in Lightbend course Fast Track to Akka with Scala
  */
trait Terminal {
  protected sealed trait Command

  protected object Command {

    case class Job(count: Int, jobType: JobType, limit: Int) extends Command

    case object Status extends Command

    case object Quit extends Command

    case class Unknown(command: String) extends Command

    def apply(command: String): Command =
      CommandParser.parseAsCommand(command)
  }

  private object CommandParser extends RegexParsers {

    def parseAsCommand(s: String): Command =
      parseAll(parser, s) match {
        case Success(command, _) => command
        case _                   => Command.Unknown(s)
      }

    def createJob: Parser[Command.Job] =
      opt(int) ~ ("job|j".r ~> opt(jobtype) ~ opt(int)) ^^ {
        case count ~ (jobType ~ limit) =>
          Command.Job(
            count getOrElse 1,
            jobType getOrElse JobType.FileToFile,
            limit getOrElse Int.MaxValue
          )
      }

    def getStatus: Parser[Command.Status.type] =
      "status|s".r ^^ (_ => Command.Status)

    def quit: Parser[Command.Quit.type] =
      "quit|q".r ^^ (_ => Command.Quit)

    def jobtype: Parser[JobType] =
      "FF|FD|DF|ff|fd|df".r ^^ JobType.apply

    def int: Parser[Int] =
      """\d+""".r ^^ (_.toInt)
  }

  private val parser: CommandParser.Parser[Command] =
    CommandParser.createJob | CommandParser.getStatus | CommandParser.quit
}
