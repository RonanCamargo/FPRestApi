package infrastructure.persistence.runner

import cats.effect.IO
import fs2.Stream
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database

case class DatabaseRunner(db: Database) {

  def run[A](action: DBIO[A]): IO[A] =
    IO.fromFuture { IO(db.run(action)) }
      .attempt
      .flatMap {
        case Right(value) => IO.pure(value)
        case Left(e)      => IO.raiseError(e)
      }

  def runStreaming[A](action: DBIO[Seq[A]]): Stream[IO, A] =
    Stream.evals(run(action))
}

object DatabaseRunner {
  implicit class DatabaseRunnerOps[A](action: DBIO[A]) {
    def runWith(runner: DatabaseRunner): IO[A] = runner.run(action)

    def run(implicit runner: DatabaseRunner): IO[A] = runner.run(action)
  }

  implicit class DatabaseRunnerStreamingOps[A](action: DBIO[Seq[A]]) {
    def stream(implicit runner: DatabaseRunner): Stream[IO, A] =
      runner.runStreaming(action)
  }
}
