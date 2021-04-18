package http

import cats.effect.IO
import cats.syntax.semigroup._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import persistence.entities.Person
import persistence.repositories.PersonRepository
import persistence.runner.DatabaseRunner
import persistence.runner.DatabaseRunner.DatabaseRunnerOps

case class PersonService(
    personRepository: PersonRepository,
    dbRunner: DatabaseRunner
) extends HttpService[IO] {

  def routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] { get |+| post |+| put |+| delete }

  def get: Handler = {
    case GET -> Root / IntVar(id) =>
      personRepository
        .findBy(id)
        .runWith(dbRunner)
        .flatMap {
          case None => NotFound(s"Cannot find a person with id: $id")
          case p    => Ok(p.get)
        }
  }

  def post: Handler = {
    case body @ POST -> Root =>
      for {
        p <- body.as[Person]
        save = personRepository.save(p).runWith(dbRunner)
        rs <- Created(save)
      } yield rs
  }

  def put: Handler = {
    case body @ PUT -> Root =>
      for {
        p <- body.as[Person]
        update = personRepository.update(p).runWith(dbRunner)
        rs <- Ok(update)
      } yield rs
  }

  def delete: Handler = {
    case DELETE -> Root / IntVar(id) =>
      personRepository.delete(id).runWith(dbRunner) >> Ok()
  }
}
