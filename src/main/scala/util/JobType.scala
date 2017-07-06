package util

/**
  * Created by dustin-vannoy on 7/6/17.
  */

sealed trait JobType

object JobType {

  case object FileToFile extends JobType
  case object FileToDb extends JobType
  case object DbToFile extends JobType

  def apply(code: String): JobType =
    code.toLowerCase match {
      case "ff" => FileToFile
      case "fd" => FileToDb
      case "df" => DbToFile
      case _ => throw new IllegalArgumentException(s"""Unkown value $code""")
    }
}
