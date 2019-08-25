package mscoket.impl

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import io.bullet.borer.{Decoder, Encoder}
import msocket.api.{EitherCodecs, PostClient}

import scala.concurrent.Future

class PostClientJvm(uri: Uri)(implicit actorSystem: ActorSystem) extends PostClient with HttpCodecs with EitherCodecs {
  import actorSystem.dispatcher
  implicit lazy val mat: Materializer = ActorMaterializer()
  override def requestResponse[Req: Encoder, Res: Decoder](req: Req): Future[Res] = {
    Marshal(req).to[RequestEntity].flatMap { requestEntity =>
      val request = HttpRequest(HttpMethods.POST, uri = uri, entity = requestEntity)
      Http().singleRequest(request).flatMap { response =>
        //todo: make generic status checks and then test if required
        Unmarshal(response.entity).to[Res]
      }
    }
  }

  override def requestStream[Req: Encoder, Res: Decoder](req: Req): Source[Res, NotUsed] = {
    val futureSource = Marshal(req).to[RequestEntity].flatMap { requestEntity =>
      val request = HttpRequest(HttpMethods.POST, uri = uri, entity = requestEntity)
      Http().singleRequest(request).flatMap { response =>
        //todo: make generic status checks and then test if required
        Unmarshal(response.entity).to[Source[Res, NotUsed]]
      }
    }
    Source.fromFutureSource(futureSource).mapMaterializedValue(_ => NotUsed)
  }
}
