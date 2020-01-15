package msocket.impl.post

import io.bullet.borer.{Decoder, Encoder, Json, Target}
import msocket.api.Encoding.JsonText
import msocket.api.{ErrorProtocol, Subscription}
import msocket.impl.JsTransport
import org.scalajs.dom.experimental.ReadableStreamReader

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.timers
import scala.util.control.NonFatal

class HttpPostTransportJs[Req: Encoder: ErrorProtocol, CT <: Target](uri: String)(
    implicit encoders: HttpJsEncoders[CT],
    ec: ExecutionContext,
    streamingDelay: FiniteDuration
) extends JsTransport[Req] {

  override def requestResponse[Res: Decoder: Encoder](req: Req): Future[Res] =
    FetchHelper.postRequest(uri, req).flatMap(response => encoders.response(response))

  override def requestStream[Res: Decoder: Encoder](request: Req, onMessage: Res => Unit, onError: Throwable => Unit): Subscription = {
    val readerF: Future[ReadableStreamReader[js.Object]] = FetchHelper.postRequest[Req, Json.type](uri, request).map { response =>
      val reader = new CanNdJsonStream(response.body).getReader()
      def read(): Unit = {
        reader.read().toFuture.foreach { chunk =>
          if (!chunk.done) {
            val jsonString = FetchEventJs(chunk.value).data
            if (jsonString != "") {
              try onMessage(JsonText.decodeWithError(jsonString))
              catch {
                case NonFatal(ex) => onError(ex); reader.cancel(ex.getMessage)
              }
            }
            timers.setTimeout(streamingDelay) {
              read()
            }
          }
        }
      }

      read()

      reader
    }

    () => readerF.foreach(_.cancel("cancelled"))
  }

}
