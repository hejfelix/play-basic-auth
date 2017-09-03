package com.lambdaminute.playbasicauth

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest._
import play.api.libs.typedmap.TypedMap
import play.api.mvc.Results.Ok
import play.api.mvc.Results.Unauthorized
import play.api.mvc.request.{RemoteConnection, RequestTarget}
import play.api.mvc.{Headers, RequestHeader}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class BasicAuthFilterSpec extends FlatSpec with Matchers {

  val username = "Felix"
  val password = "S3cretPazzw0rd"
  val realm    = "someRealm"

  implicit val actorSystem  = ActorSystem()
  implicit val materializer = ActorMaterializer()
  private val freeRoutes    = List("/status")
  private val users         = List(User(username, password))
  val config                = BasicAuthFilterConfig(users, realm, freeRoutes)
  val filter                = new BasicAuthFilter(config)

  val requestHeader: RequestHeader = createRequestHeaders(username, password)

  val OkFilter = (_: RequestHeader) => Future.successful(Ok)

  "Good credentials" should "give 200 ok" in {

    val result = Await.result(filter.apply(OkFilter)(requestHeader), 1.second)
    result shouldBe Ok
  }

  "Bad credentials" should "give 401 unauthorized" in {
    val badCredentialsHeader: RequestHeader = createRequestHeaders(username, "not_the_right_password")
    val unAuthedResult                      = Await.result(filter.apply(OkFilter)(badCredentialsHeader), 1.second)
    val badHeader                           = "WWW-Authenticate" -> s"""Basic realm="$realm""""
    val expectedResult                      = Unauthorized("Unauthorized").withHeaders(badHeader)
    unAuthedResult shouldBe expectedResult
  }

  "Bad credentials with /status target path" should "give 200 ok" in {
    val badCredentialsStatusPath: RequestHeader = createRequestHeaders(username, "not_the_right_password", "/status")
    val unAuthedStatusResult                    = Await.result(filter.apply(OkFilter)(badCredentialsStatusPath), 1.second)
    unAuthedStatusResult shouldBe Ok
  }

  private def createRequestHeaders(username: String, password: String, targetPath: String = "somePath") = {
    val colonSeparated = s"$username:$password"
    val encoded        = Base64.getUrlEncoder.encodeToString(colonSeparated.getBytes(StandardCharsets.UTF_8))
    val fakeHeader     = new Headers(Seq(("Authorization", s"Basic $encoded")))
    val requestTarget = new RequestTarget {
      override def uri       = ???
      override def uriString = ???
      override def path      = targetPath
      override def queryMap  = ???
    }
    val requestHeader = new RequestHeader {
      override def connection: RemoteConnection = ???
      override def method: String               = ???
      override def version: String              = ???
      override def attrs: TypedMap              = ???
      override def headers: Headers             = fakeHeader
      override def target: RequestTarget        = requestTarget
    }
    requestHeader
  }
}
