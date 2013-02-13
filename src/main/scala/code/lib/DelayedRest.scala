package code.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.actor.LAFuture

/**
 * Created with IntelliJ IDEA.
 * User: dpp
 * Date: 2/12/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
object DelayedRest extends RestHelper {
  serve {
    case "delay" :: Nil Get _ =>
    LAFuture(() => {
      Thread.sleep(2000)
      <b>Hello</b>})
  }
}
