package util

/**
  * Created by dustin-vannoy on 7/6/17.
  */

sealed trait JobInstruction

object JobInstruction {

  case object NoInstruction extends JobInstruction
  case object PrintFileInstruction extends JobInstruction
  case object CopyFileInstruction extends JobInstruction

}