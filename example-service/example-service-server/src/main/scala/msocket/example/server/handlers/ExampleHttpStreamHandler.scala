package msocket.example.server.handlers

import akka.NotUsed
import akka.stream.scaladsl.Source
import csw.example.api.ExampleApi
import csw.example.api.protocol.ExampleCodecs._
import csw.example.api.protocol.ExampleRequest._
import msocket.impl.post.{FetchEvent, HttpStreamHandler, ServerHttpCodecs}

/**
 * Implements HttpPostHandler for all messages in the protocol (requestResponse + requestStream)
 * These handlers receive POST requests and responds via [[Route]] instance
 */
class ExampleHttpStreamHandler(exampleApi: ExampleApi) extends HttpStreamHandler[ExampleRequestStream] with ServerHttpCodecs {
  override def handle(request: ExampleRequestStream): Source[FetchEvent, NotUsed] = request match {
    case Square(number)          => stream(exampleApi.square(number)) // streams a Future returned by square, see square API doc
    case HelloStream(name)       => stream(exampleApi.helloStream(name))
    case GetNumbers(divisibleBy) => stream(exampleApi.getNumbers(divisibleBy))
    case RandomBagStream         => stream(exampleApi.randomBagStream())
  }
}
