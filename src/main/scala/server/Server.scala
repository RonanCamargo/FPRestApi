package server

import cats.effect.{ExitCode, IO, IOApp}
import http.{HelloWorldService, HttpService, PersonService}
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import persistence.repositories.PersonRepository
import persistence.runner.DatabaseRunner
import persistence.setup.SetupInMemoryDatabase

import scala.concurrent.ExecutionContext

object Server extends IOApp {

  private val ExContext = ExecutionContext.global

  private val PersonRepository = new PersonRepository()
  private val DbRunner: DatabaseRunner = DatabaseRunner()

  private val InMemoryDB: SetupInMemoryDatabase = SetupInMemoryDatabase(DbRunner)

  private val HelloWorld: HttpService[IO] = HelloWorldService()
  private val Person: HttpService[IO] = PersonService(PersonRepository)(DbRunner)

  private val ServiceRouter: HttpApp[IO] = Router(
    "/hello" -> HelloWorld.routes,
    "/persons" -> Person.routes
  ).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    InMemoryDB.setup *>
      BlazeServerBuilder[IO](ExContext)
        .bindHttp(8080, "localhost")
        .withHttpApp(ServiceRouter)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
}
