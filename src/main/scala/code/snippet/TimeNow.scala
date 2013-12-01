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
