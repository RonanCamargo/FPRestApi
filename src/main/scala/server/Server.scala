package server

import cats.effect.{ExitCode, IO, IOApp}
import http.{HelloWorldService, HttpService, PersonService}
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import persistence.repositories.PersonRepository
import persistence.runner.DatabaseRunner
import persistence.setup.SetupInMemoryDatabase

import scala.concurrent.ExecutionContext

object Server extends IOApp {

  private val executionContext = ExecutionContext.global

  private val personRepository = new PersonRepository()
  private val dbRunner: DatabaseRunner = DatabaseRunner()

  private val h2Db: SetupInMemoryDatabase = SetupInMemoryDatabase(dbRunner)

  private val helloWorld: HttpService[IO] = HelloWorldService()
  private val person: HttpService[IO] = PersonService(personRepository)(dbRunner)

  private val router: HttpApp[IO] = Router(
    "/hello" -> helloWorld.routes,
    "/persons" -> person.routes
  ).orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    h2Db.setup >>
      BlazeServerBuilder[IO](executionContext)
        .bindHttp(8080, "localhost")
        .withHttpApp(router)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
  }
}
