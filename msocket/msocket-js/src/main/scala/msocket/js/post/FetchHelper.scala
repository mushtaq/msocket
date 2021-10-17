package msocket.js.post

import io.bullet.borer.Encoder
import msocket.api.models.{ErrorType, ServiceError}
import msocket.api.{ContentType, ErrorProtocol}
import msocket.js.post.HttpJsExtensions.HttpJsEncoding
import org.scalajs.dom.experimental.{Fetch, HttpMethod, RequestInit, Response}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js

object FetchHelper {
  def postRequest[Req: Encoder](uri: String, req: Req, contentType: ContentType)(implicit
      ec: ExecutionContext,
      ep: ErrorProtocol[Req]
  ): Future[Response] = {

    val fetchRequest = new RequestInit {
      method = HttpMethod.POST
      body = contentType.body(req)
      headers = js.Dictionary("content-type" -> contentType.mimeType)
    }

    def transportError(response: Response): Future[HttpError] = {
      response.text().toFuture.map(body => HttpError(response.status, response.statusText, body))
    }

    Fetch.fetch(uri, fetchRequest).toFuture.flatMap { response =>
      response.status match {
        case 200 => Future.successful(response)
        case 500 =>
          val maybeErrorType = ErrorType.from(response.headers.get("Error-Type"))
          val errorF         = maybeErrorType match {
            case ErrorType.DomainError => contentType.response[ep.E, Req](response)
            case _                     => contentType.response[ServiceError, Req](response)
          }
          errorF.map(throw _)
        case _   => transportError(response).map(throw _)
      }
    }
  }
}
