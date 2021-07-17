package presentation.controllers

import cats.effect.IO
import cats.syntax.option._
import cats.syntax.semigroup._
import infrastructure.json.entities.PersonCodec._
import infrastructure.persistence.entities.PersonRow
import infrastructure.persistence.repositories.PersonRepository
import infrastructure.persistence.runner.DatabaseRunner
import infrastructure.persistence.runner.DatabaseRunner.DatabaseRunnerOps
import monocle.syntax.all._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._

case class PersonController(
    personRepository: PersonRepository
)(implicit dbRunner: DatabaseRunner)
    extends HttpController[IO] {

  def routes: HttpRoutes[IO] =
    HttpRoutes.of[IO] { get |+| post |+| put |+| delete }

  def get: Handler = {
    case GET -> Root / IntVar(id) =>
      personRepository
        .find(id)
        .run
        .flatMap {
          case Some(person) => Ok(person)
          case None         => NotFound(s"Cannot find a person with id: $id")
        }
  }

  def post: Handler = {
    case body @ POST -> Root =>
      for {
        p <- body.as[PersonRow]
        save = personRepository.save(p).run
        rs <- Created(save)
      } yield rs
  }

  def put: Handler = {
    case body @ PUT -> Root / IntVar(id) =>
      for {
        p <- body.as[PersonRow]
        update = personRepository.update(p.focus(_.id).replace(id.some)).run
        rs <- Ok(update)
      } yield rs
  }

  def delete: Handler = {
    case DELETE -> Root / IntVar(id) =>
      personRepository.delete(id).run *> Ok()
  }
}
