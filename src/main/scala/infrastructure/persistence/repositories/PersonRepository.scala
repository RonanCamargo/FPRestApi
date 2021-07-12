package infrastructure.persistence.repositories

import domain.repositories.FPersonRepository
import infrastructure.persistence.entities.PersonRow
import infrastructure.persistence.tables.PersonTable
import monocle.syntax.all._
import slick.jdbc.H2Profile.api._
import cats.syntax.option._
import scala.concurrent.ExecutionContext

class PersonRepository(implicit executionContext: ExecutionContext)
    extends FPersonRepository[DBIO, Int, PersonRow] {

  private lazy val persons = PersonTable.table

  private def saveAndGetId(person: PersonRow): DBIO[Int] =
    (persons returning persons.map(_.id)) += person

  def save(
      person: PersonRow
  ): DBIO[PersonRow] =
    saveAndGetId(person)
      .map { id => person.focus(_.id).replace(id.some) }

  def find(id: Int): DBIO[Option[PersonRow]] =
    persons.filter(_.id === id).take(num = 1).result.headOption

  def update(person: PersonRow): DBIO[Int] =
    persons.update(person)

  def delete(id: Int): DBIO[Int] = persons.filter(_.id === id).delete

}
