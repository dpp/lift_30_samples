package code.snippet

import xml.NodeSeq
import net.liftweb._
import http._
import js.JE.JsRaw
import js.JsCmds.Script


/**
 * Created with IntelliJ IDEA.
 * User: dpp
 * Date: 3/26/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
object PromisingStuff {
def render: NodeSeq =
  (for {
    sess <- S.session
  } yield Script(JsRaw("var myFuncs = "+sess.buildRoundtrip(List(
    "thing" -> thing _)).toJsCmd).cmd)) openOr NodeSeq.Empty

  def thing(s: String): Stream[String] = {
    var x = 0
    (s + x) #:: {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      {Thread.sleep(1000); x += 1; s + x} #::
      Stream.empty[String]
  }
}
