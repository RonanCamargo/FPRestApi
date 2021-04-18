package http

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

case class HelloWorldService() extends HttpService[IO] {

  def routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" / name =>
        Ok(s"Hello, $name.")
    }
}
