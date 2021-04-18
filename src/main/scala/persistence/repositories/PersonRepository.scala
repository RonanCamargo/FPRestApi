package persistence.repositories

import cats.syntax.option._
import monocle.syntax.all._
import persistence.entities.Person
import persistence.tables.PersonTable
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext

class PersonRepository {

  private lazy val persons = PersonTable.table

  def save(person: Person): DBIO[Int] = (persons returning persons.map(_.id.get)) += person

  def saveAndGet(person: Person)(implicit ec: ExecutionContext): DBIO[Person] =
    save(person)
      .map{ id => person.focus(_.id).replace(id.some) }

  def findBy(id: Int): DBIO[Option[Person]] = persons.filter(_.id === id).take(1).result.headOption

}
