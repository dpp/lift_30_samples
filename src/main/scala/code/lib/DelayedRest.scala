package code.lib

import net.liftweb.http.rest.RestHelper
import net.liftweb.actor.LAFuture
import net.liftweb.util.Helpers
import net.liftweb.http.{ForbiddenResponse, LiftResponse, Req, LiftRules}
import net.liftweb.common.{Full, Box}

object DelayedRest extends RestHelper {
  serve {
    case "delay" :: Nil Get _ =>
      LAFuture.build({
        Thread.sleep(2000)
        <b>Hello</b>
      })

    case "delay" :: Helpers.AsInt(delay) :: Nil Get _ if delay > 0 =>
      LAFuture(() => {
        Thread.sleep(delay.toLong * 1000L)
        <i>I slept for
          {delay}
          seconds</i>
      })
  }
}

object Guards {
  def isOkay_? = Helpers.randomInt(100) > 50

  def guardAs404(in: LiftRules.DispatchPF): LiftRules.DispatchPF =
    new LiftRules.DispatchPF {
      def apply(v1: Req): () => Box[LiftResponse] = in(v1)

      def isDefinedAt(x: Req): Boolean = isOkay_? && in.isDefinedAt(x)
    }

  def guardAs403(in: LiftRules.DispatchPF): LiftRules.DispatchPF =
    new LiftRules.DispatchPF {
      def apply(v1: Req): () => Box[LiftResponse] =
        if (!isOkay_?)
          () => Full(ForbiddenResponse("Verboten"))
        else
          in(v1)

      def isDefinedAt(x: Req): Boolean = in.isDefinedAt(x)
    }
}