package persistence.tables

import persistence.entities.Person
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

class PersonTable(tag: Tag) extends Table[Person](tag, "PERSONS"){

  def id: Rep[Option[Int]] = column[Option[Int]]("ID", O.AutoInc, O.PrimaryKey)
  def name: Rep[String] = column[String]("NAME")
  def age: Rep[Int] = column[Int]("AGE")

  def * : ProvenShape[Person] = (id, name, age) <> (Person.tupled, Person.unapply)
}

object PersonTable {
  val table = TableQuery[PersonTable]
}
