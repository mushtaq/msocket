package msocket.api

import io.bullet.borer.Dom.{Element, StringElem}
import io.bullet.borer.{Decoder, Encoder}

import scala.reflect.ClassTag

trait ErrorProtocol[T] {
  type E <: Throwable
  def enc: Encoder[E]
  def dec: Decoder[E]
  def classTag: ClassTag[E]
}

object ErrorProtocol {
  implicit def dec[T](implicit ep: ErrorProtocol[T]): Decoder[ep.E]       = ep.dec
  implicit def enc[T](implicit ep: ErrorProtocol[T]): Encoder[ep.E]       = ep.enc
  implicit def classTag[T](implicit ep: ErrorProtocol[T]): ClassTag[ep.E] = ep.classTag

  def bind[T, Err <: Throwable: Encoder: Decoder: ClassTag]: ErrorProtocol[T] =
    new ErrorProtocol[T] {
      override type E = Err
      override val enc: Encoder[E]       = enrich(Encoder[E])
      override val dec: Decoder[E]       = Decoder[E]
      override val classTag: ClassTag[E] = scala.reflect.classTag[E]
    }

  private def enrich[E <: Throwable](encoder: Encoder[E]): Encoder[E] = {
    Encoder[Map[String, Element]].contramapWithWriter { (w, error) =>
      val bytes   = w.target.encode(error)(encoder).toByteArray
      val mapElem = w.target.decode(bytes).to[Map[String, Element]].value
      mapElem + ("error_message" -> StringElem(error.getMessage))
    }
  }
}
