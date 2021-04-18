package persistence.setup

import cats.effect.IO
import persistence.runner.DatabaseRunner
import persistence.tables.PersonTable
import slick.jdbc.H2Profile.api._

case class SetupInMemoryDatabase(dbRunner: DatabaseRunner) {
  val persons = PersonTable.table

  def setup: IO[Unit] = {
    val createSchema = persons.schema.dropIfExists >> persons.schema.create
    dbRunner.run(createSchema)
  }
}
