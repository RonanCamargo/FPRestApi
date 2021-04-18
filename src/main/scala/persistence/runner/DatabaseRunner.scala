package persistence.runner

import cats.effect.IO
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database

case class DatabaseRunner() {
  private lazy val db = Database.forConfig("h2mem1")

  def run[A](dbio: DBIO[A]): IO[A] = {
    IO.fromFuture(IO(db.run(dbio))).attempt.flatMap {
      case Right(value) => IO.pure(value)
      case Left(e)      => IO.raiseError(e)
    }
  }
}

object DatabaseRunner {
  implicit class DatabaseRunnerOps[A](dbio: DBIO[A]) {
    def runWith(h2Runner: DatabaseRunner): IO[A] = h2Runner.run(dbio)
  }
}
