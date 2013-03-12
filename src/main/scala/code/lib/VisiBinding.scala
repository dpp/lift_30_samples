package code.lib

import net.liftweb.http.LiftRules
import xml.{Elem, NodeSeq}
import visi.core.{PrimDouble, TPrim, Visi}
import net.liftweb.common.{Failure, Full}
import net.liftweb.util._
import Helpers._

/**
 * Created with IntelliJ IDEA.
 * User: dpp
 * Date: 3/11/13
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
object VisiBinding {
  /**
   * Call during Boot. wire the Visi stuff into the tempalting engine
   */
  def init() {
    LiftRules.tagProcessor.prepend {
      case ("script", e, session) if e.attribute("data-visi").isDefined =>
        doCompilation(e)
    }
  }

  def fixIndent(str: String): String = {
    val lst = str.replace("\r\n", "\n").replace("\r", "\n").split('\n').toList.map(s => if (s.trim.length == 0) "" else s)
    def countSpace(s: String): Int = s.takeWhile(_ == ' ').length
    val max = lst.foldLeft(0)((len, s) => Math.max(len, countSpace(s)))
    lst.map(s => if (s.length == 0) s else s.substring(max)).mkString("\n")
  }

  def doCompilation(e: Elem): NodeSeq =
  Visi.compileWithRunnableInfo(fixIndent(e.text)) match {
    case Full((js, info)) =>
      val name = Helpers.nextFuncName
      <lift:tail><script>
      var {name} = new function() {{
      {
      visi.core.Compiler.jsLibrary +
      js}

        this.updateWorld = function(info) {{
        var res = info.success;
        if (res != null) {{
        for (var i in res) {{
          $('#'+i).text(res[i].toString());
        }}
        }}
        }}
        }};

        $(document).ready(function() {{
        {
        "var ff = "+name+";\n" +
        info.sources.map(s => "$("+("#"+s.name).encJs+").keyup(function(eo) {\nff.updateWorld(ff.$_exec_maybe({" +
          s.name.encJs+":" + {
          s.tpe match {
            case TPrim(PrimDouble) => "parseFloat("
            case _ => "("
          }
        } + "eo.target.value)"+
          "}));\n});").mkString(";\n")

        }

        }});

      </script></lift:tail>
    case Failure(msg, _, _) => <div>Visi compilation failed: {msg}</div>
    case _ => <div>Unknown Visi issue</div>
  }
}
