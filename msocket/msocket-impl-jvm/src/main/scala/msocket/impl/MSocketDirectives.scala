package msocket.impl

import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, ExceptionHandler}
import msocket.api.ErrorType
import msocket.api.models.ServiceException
import msocket.impl.post.ServerHttpCodecs

import scala.util.control.NonFatal

object MSocketDirectives {
  import ServerHttpCodecs._

  def addMissingAcceptHeader(request: HttpRequest): HttpRequest = {
    request.header[Accept] match {
      case Some(_) => request
      case None    => request.addHeader(Accept(request.entity.contentType.mediaType))
    }
  }

  val withAcceptHeader: Directive0 = mapRequest(addMissingAcceptHeader)

  def withExceptionHandler[Req](implicit et: ErrorType[Req]): Directive0 = handleExceptions {
    ExceptionHandler {
      case NonFatal(ex: et.E) => complete(StatusCodes.InternalServerError -> ex)
      case NonFatal(ex)       => complete(StatusCodes.InternalServerError -> ServiceException.fromThrowable(ex))
    }
  }

}
