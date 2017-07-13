package util

import util.JobInstruction.NoInstruction

import scala.util.parsing.combinator.RegexParsers

/**
  * Created by dustin-vannoy on 7/6/17.
  *   Based on Terminal trait used in Lightbend course Fast Track to Akka with Scala
  */
trait Terminal {
  protected sealed trait Command

  protected object Command {

    case class Job(id: String) extends Command

    case class JobCommand(jobId: String, instruction: JobInstruction) extends Command

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
      ("job|j".r ~> opt(str)) ^^ {
        case id =>
          Command.Job(
            id getOrElse "a"
          )
      }

    def triggerJobCommand: Parser[Command.JobCommand] =
      ("jobCommand|jc".r ~> opt(str) ~ opt(jobinstruction)) ^^ { // ~ opt(parameter) ~ opt(parameter)
        case j ~ instruction =>
          Command.JobCommand(
            j getOrElse "a",
            instruction getOrElse NoInstruction
          )
      }

    def quit: Parser[Command.Quit.type] =
      "quit|q".r ^^ (_ => Command.Quit)

    def jobinstruction: Parser[JobInstruction] =
    "NI|ni|PF|pf|SF|sf".r ^^ JobInstruction.apply

    def int: Parser[Int] =
      """\d+""".r ^^ (_.toInt)

    def str: Parser[String] =
      "[a-zA-Z0-9_]*".r ^^ (s => s.toString)//"""\\S+""".r ^^ (_.toString)

    //    def getStatus: Parser[Command.Status.type] =
    //      "status|s".r ^^ (_ => Command.Status)
  }

  private val parser: CommandParser.Parser[Command] =
    CommandParser.triggerJobCommand | CommandParser.createJob  | CommandParser.quit
    // | CommandParser.getStatus |

}
