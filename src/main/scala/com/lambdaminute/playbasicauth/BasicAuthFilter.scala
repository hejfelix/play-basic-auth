package com.lambdaminute.playbasicauth

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.stream.Materializer
import play.api.mvc.Results._
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future

case class User(name: String, password: String) {
  def toColonSeparatedString = s"$name:$password"
}

class BasicAuthFilter(userCredentials: List[User], realm: String)(
    implicit val mat: Materializer)
    extends Filter {

  private val users =
    userCredentials.map(_.toColonSeparatedString).map(base64Encode)

  private val unauthorized = Future.successful {
    Unauthorized("Unauthorized").withHeaders(
      "WWW-Authenticate" -> s"""Basic realm="${realm}"""")
  }

  def apply(nextFilter: RequestHeader => Future[Result])(
      requestHeader: RequestHeader): Future[Result] =
    requestHeader.headers.get("Authorization") match {
      case Some(authHeader) if validUser(users, authHeader) =>
        nextFilter(requestHeader)
      case _ if requestHeader.path == "/status" => nextFilter(requestHeader)
      case _                                    => unauthorized
    }

  private def validUser(users: List[String], authHeader: String) =
    users.contains(authHeader.stripPrefix("Basic "))

  private def base64Encode(input: String) =
    Base64.getUrlEncoder.encodeToString(input.getBytes(StandardCharsets.UTF_8))
}
