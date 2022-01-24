package msocket.http.ws

import akka.http.scaladsl.model.ws.Message
import io.bullet.borer.Encoder
import msocket.api.models.ResponseHeaders
import msocket.api.{ContentType, ErrorProtocol}
import WebsocketExtensions.WebsocketEncoding
import msocket.jvm.stream.StreamResponseEncoder
import msocket.security.AccessController

/**
 * This helper class can be extended to define custom  websocket handler in the server which returns [[Source]] of [[Message]].
 * WebsocketHandler takes a request type which will be bound to Domain specific error using ErrorProtocol.
 */
class WebsocketStreamResponseEncoder[Req: ErrorProtocol](contentType: ContentType, val accessController: AccessController)
    extends StreamResponseEncoder[Req, Message] {
  override def encode[Res: Encoder](response: Res, headers: ResponseHeaders): Message = contentType.strictMessage[Res](response)
}
