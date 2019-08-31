package csw.simple.api.client

import akka.NotUsed
import akka.stream.scaladsl.Source
import csw.simple.api.SimpleRequest._
import csw.simple.api._
import msocket.api.RequestClient

import scala.concurrent.Future

class SimpleClient(postClient: RequestClient[SimpleRequest]) extends SimpleApi with Codecs {
  override def hello(name: String): Future[String]                = postClient.requestResponse[String](Hello(name))
  override def square(number: Int): Future[Int]                   = postClient.requestResponseWithDelay[Int](Square(number))
  override def helloStream(name: String): Source[String, NotUsed] = postClient.requestStream[String](HelloStream(name))
  override def getNumbers(divisibleBy: Int): Source[Int, Future[Option[String]]] = {
    postClient.requestStreamWithError[Int, String](GetNumbers(divisibleBy))
  }
}
