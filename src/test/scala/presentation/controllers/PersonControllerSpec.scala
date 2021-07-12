package presentation.controllers

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import infrastructure.persistence.entities.PersonRow
import infrastructure.persistence.repositories.PersonRepository
import infrastructure.persistence.runner.DatabaseRunner
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.implicits._
import org.scalamock.scalatest.MockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import slick.dbio.DBIO

class PersonControllerSpec extends AnyWordSpec with Matchers with MockFactory {

  private val personRepository = stub[PersonRepository]
  private val dbRunner = mock[DatabaseRunner]

  private val subject = PersonController(personRepository)(dbRunner)

  "#get" when {
    "asking for an existent person" should {
      "return an Ok response" in {
        val expectedPerson: Option[PersonRow] =
          PersonRow(Some(1), "Ronan", 27).some
        val personId = 1

        val request: Request[IO] =
          Request[IO](method = Method.GET, Uri.unsafeFromString(s"/$personId"))

        (dbRunner
          .run(_: DBIO[Option[PersonRow]]))
          .expects(*)
          .returning(IO.pure(expectedPerson))

        val result: Response[IO] =
          subject.routes.orNotFound.run(request).unsafeRunSync

        result.status shouldBe Status.Ok
        result.as[PersonRow].unsafeRunSync() shouldBe expectedPerson.get

      }
    }
  }

  "#post" when {
    "passing a valid person" should {
      "" in {
        val expectedPerson: PersonRow =
          PersonRow(1.some, "Ronan", 27)

        val request: Request[IO] =
          Request[IO](method = Method.POST).withEntity(expectedPerson)

        (dbRunner
          .run(_: DBIO[PersonRow]))
          .expects(*)
          .returning(IO.pure(expectedPerson))

        val result: Response[IO] =
          subject.routes.orNotFound.run(request).unsafeRunSync

        result.status shouldBe Status.Created
        result.as[PersonRow].unsafeRunSync() shouldBe expectedPerson

      }
    }
  }
}
