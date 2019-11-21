package msocket.api

import akka.stream.scaladsl.Source
import io.bullet.borer.{Decoder, Encoder}
import msocket.api.models.Subscription
import msocket.api.utils.InterceptedTransport

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

abstract class Transport[Req: Encoder] {
  def requestResponse[Res: Decoder](request: Req): Future[Res]
  def requestResponse[Res: Decoder](request: Req, timeout: FiniteDuration): Future[Res]
  def requestStream[Res: Decoder](request: Req): Source[Res, Subscription]

  def interceptRequest(action: Req => Unit): Transport[Req] = new InterceptedTransport(this, action)
}
