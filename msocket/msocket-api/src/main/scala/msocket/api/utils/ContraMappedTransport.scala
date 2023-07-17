package msocket.api.utils

import org.apache.pekko.stream.scaladsl.Source
import io.bullet.borer.{Decoder, Encoder}
import msocket.api.{ErrorProtocol, Subscription, Transport}
import msocket.portable.Observer

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class ContraMappedTransport[A, B: Encoder: ErrorProtocol](transport: Transport[A], contraF: B => A) extends Transport[B] {
  override def requestResponse[Res: Decoder: Encoder](request: B): Future[Res] = {
    transport.requestResponse(contraF(request))
  }

  override def requestResponse[Res: Decoder: Encoder](request: B, timeout: FiniteDuration): Future[Res] = {
    transport.requestResponse(contraF(request), timeout)
  }

  override def requestStream[Res: Decoder: Encoder](request: B): Source[Res, Subscription] = {
    transport.requestStream(contraF(request))
  }

  override def requestStream[Res: Decoder: Encoder](request: B, observer: Observer[Res]): Subscription = {
    transport.requestStream(contraF(request), observer)
  }
}
