package http

import cats.kernel.Semigroup
import org.http4s.{HttpRoutes, Request, Response}

trait HttpService[F[_]] {

  type Handler = PartialFunction[Request[F], F[Response[F]]]
  implicit val handlerSemigroup: Semigroup[Handler] =
    Semigroup.instance[Handler] { (h1, h2) => h1 orElse h2 }

  def routes: HttpRoutes[F]
}
