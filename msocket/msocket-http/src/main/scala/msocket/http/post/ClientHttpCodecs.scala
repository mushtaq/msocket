package msocket.http.post

import org.apache.pekko.http.scaladsl.marshalling.ToEntityMarshaller
import io.bullet.borer.Encoder
import msocket.api.ContentType

trait ClientHttpCodecs extends ServerHttpCodecs {
  def clientContentType: ContentType

  override implicit def borerToEntityMarshaller[T: Encoder]: ToEntityMarshaller[T] = borerMarshaller(prefer = clientContentType.target)
}
