package util

/**
  * Created by dustin-vannoy on 7/6/17.
  */

sealed trait JobInstruction

object JobInstruction {

  case object NoInstruction extends JobInstruction
  case object PrintFileInstruction extends JobInstruction
  case object CopyFileInstruction extends JobInstruction

  def apply(code: String): JobInstruction =
    code.toLowerCase match {
      case "ni" => NoInstruction
      case "pf" => PrintFileInstruction
      case "sf" => CopyFileInstruction
      case _ => throw new IllegalArgumentException(s"""Unkown value $code""")
    }



}