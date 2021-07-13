package infrastructure.persistence.repositories

import domain.repositories.FPersonRepository
import infrastructure.persistence.entities.PersonRow
import infrastructure.persistence.tables.PersonTable
import monocle.syntax.all._
import slick.jdbc.H2Profile.api._
import cats.syntax.option._
import scala.concurrent.ExecutionContext

case class PersonRepository(implicit executionContext: ExecutionContext)
    extends FPersonRepository[DBIO, Int, PersonRow] {

  private lazy val persons = PersonTable.table

  def save(person: PersonRow): DBIO[PersonRow] = saveAndGetId(person).map { id => person.focus(_.id).replace(id.some) }

  def find(id: Int): DBIO[Option[PersonRow]] = filterById(id).take(num = 1).result.headOption

  def update(person: PersonRow): DBIO[Int] = persons.update(person)

  def delete(id: Int): DBIO[Int] = filterById(id).delete

  private def saveAndGetId(person: PersonRow): DBIO[Int] = (persons returning persons.map(_.id)) += person

  private def filterById(id: Int): Query[PersonTable, PersonRow, Seq] = persons.filter(_.id === id)
}
