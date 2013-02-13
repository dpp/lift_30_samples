package code.snippet

import xml.NodeSeq
import net.liftweb.http.js.JsCmds.Script
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.{ScopedLiftActor, S}
import net.liftweb.json.JsonAST.JString

object ActNice {
  def render: NodeSeq = {
    (for {
      sess <- S.session
    } yield {
      // get a server-side actor that when we send
      // a JSON serializable object, it will send it to the client
      // and call the named function with the parameter
      val clientProxy = sess.serverActorForClient("changeNode")

      // Create a server-side Actor that will receive messaes when
      // a function on the client is called
      val serverActor = new ScopedLiftActor {
        override def lowPriority = {
          case JString(str) => clientProxy ! ("You said: " + str)
        }
      }

      Script(JsRaw("var sendToServer = " + sess.clientActorFor(serverActor).toJsCmd).cmd &
        JsRaw("function changeNode(str) {document.getElementById(\"foo\").innerHTML = str;}").cmd)
    }) openOr NodeSeq.Empty
  }
}
