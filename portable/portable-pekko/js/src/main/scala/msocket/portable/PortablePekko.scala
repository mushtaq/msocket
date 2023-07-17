package msocket.portable

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.stream.scaladsl.Source

import scala.annotation.nowarn
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import scala.scalajs.js.timers

object PortablePekko {

  def setTimeout(duration: FiniteDuration)(body: => Unit)(implicit @nowarn actorSystem: ActorSystem[_]): Unit = {
    timers.setTimeout(duration)(body)
  }

  implicit class SourceOps[Out, Mat](private val target: Source[Out, Mat]) extends AnyVal {
    def viaObserver(observer: Observer[Out])(implicit @nowarn ec: ExecutionContext): Source[Out, Mat] = {
      target.onMessage(observer)
      target
    }
  }
}
