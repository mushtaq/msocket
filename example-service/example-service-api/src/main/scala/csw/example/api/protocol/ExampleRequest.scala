package csw.example.api.protocol

import msocket.api.Labellable
import msocket.api.models.MetricLabels

import scala.reflect.ClassTag

/**
 * Transport agnostic message protocol is defined as a Scala ADT
 * For each API method, there needs to be a matching message in this ADT
 * For example, Hello(name) is the message for hello(name) method in the API
 */
sealed trait ExampleRequest

object ExampleRequest {
  // these messages are used for requestResponse interaction model
  sealed trait ExampleRequestResponse extends ExampleRequest
  case class Hello(name: String)      extends ExampleRequestResponse
  case object RandomBag               extends ExampleRequestResponse

  object ExampleRequestResponse {
    implicit val labellable: ExampleRequestResponse => Labellable[ExampleRequestResponse] =
      req =>
        Labellable.make {
          req match {
            case Hello(_)  => Map("msg" -> "Hello", "appName"     -> "Example")
            case RandomBag => Map("msg" -> "RandomBag", "appName" -> "Example")
          }
        }
  }

  // these messages are used for requestStream interaction model
  sealed trait ExampleRequestStream       extends ExampleRequest
  case class HelloStream(name: String)    extends ExampleRequestStream
  case class Square(number: Int)          extends ExampleRequestStream
  case class GetNumbers(divisibleBy: Int) extends ExampleRequestStream
  case object RandomBagStream             extends ExampleRequestStream
}
