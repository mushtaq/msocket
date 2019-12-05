package msocket.impl.sse

import akka.NotUsed
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.stream.scaladsl.Source
import io.bullet.borer.Encoder
import msocket.api.models.ServiceError
import msocket.impl.Encoding.JsonText
import msocket.impl.StreamExtensions

import scala.concurrent.duration.DurationLong
import scala.util.control.NonFatal

trait SseStreamExtensions extends StreamExtensions[ServerSentEvent] {
  override def stream[T, Mat](input: Source[T, Mat])(implicit encoder: Encoder[T]): Source[ServerSentEvent, NotUsed] = {
    input
      .map(x => ServerSentEvent(JsonText.encode(x)))
      .keepAlive(30.seconds, () => ServerSentEvent.heartbeat)
      .mapMaterializedValue(_ => NotUsed)
      .recover {
        case NonFatal(ex) => ServerSentEvent(JsonText.encode(ServiceError.fromThrowable(ex)))
      }
  }
}
