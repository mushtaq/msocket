package msocket.example.client

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import csw.example.api.client.ExampleClient
import csw.example.api.protocol.{Codecs, ExampleRequest}
import mscoket.impl.post.HttpPostTransport
import mscoket.impl.rsocket.client.RSocketTransportFactory
import mscoket.impl.sse.SseTransport
import mscoket.impl.ws.WebsocketTransport

object ClientMain extends Codecs {

  def main(args: Array[String]): Unit = {
    implicit lazy val system: ActorSystem    = ActorSystem()
    lazy implicit val mat: ActorMaterializer = ActorMaterializer()
    import system.dispatcher

    lazy val httpPostTransport  = new HttpPostTransport[ExampleRequest]("http://localhost:5000/post", None)
    lazy val sseTransport       = new SseTransport[ExampleRequest]("http://localhost:5000/sse")
    lazy val websocketTransport = new WebsocketTransport[ExampleRequest]("ws://localhost:5000/websocket")
    lazy val rSocketTransport   = new RSocketTransportFactory[ExampleRequest].transport("ws://localhost:7000")

    val exampleClient = new ExampleClient(httpPostTransport)
    new ClientApp(exampleClient).testRun()
  }

}
