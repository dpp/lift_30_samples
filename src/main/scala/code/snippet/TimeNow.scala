package code.snippet

import java.util.Date
import net.liftweb.util.Helpers._
import net.liftweb.util.ClearClearable

/**
 * Bind the current time into the body of the invoking tag
 */
class TimeNow {
  def render = "1 *+" #> (new Date).toString
}

class RandomList {
  def render = "li *" #> (0 to randomInt(10)).map(_ => "<b>"+randomString(5)+"</b>")
}

class FixedList {
  def render = "li *" #> List("1", "fish", "red", "star") & ClearClearable
}