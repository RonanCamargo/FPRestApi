package infrastructure.server

import cats.effect.{ExitCode, IO, IOApp}
import presentation.controllers.{HelloWorldController, HttpController, PersonController}
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import infrastructure.persistence.repositories.PersonRepository
import infrastructure.persistence.runner.DatabaseRunner
import infrastructure.persistence.setup.SetupInMemoryDatabase
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

object Server extends IOApp {

  private implicit val ExContext: ExecutionContext = ExecutionContext.global

  private val PersonRepository = new PersonRepository()
  private val DbRunner: DatabaseRunner = DatabaseRunner(Database.forConfig("h2mem1"))

  private val InMemoryDB: SetupInMemoryDatabase = SetupInMemoryDatabase(DbRunner)

  private val HelloWorld: HttpController[IO] = HelloWorldController()
  private val Person: HttpController[IO] = PersonController(PersonRepository)(DbRunner)

  private val ServiceRouter: HttpApp[IO] = Router(
    "/hello" -> HelloWorld.routes,
    "/persons" -> Person.routes
  ).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    InMemoryDB.setup *>
      BlazeServerBuilder[IO](ExContext)
        .bindHttp(port = 8080, host = "localhost")
        .withHttpApp(ServiceRouter)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
}
