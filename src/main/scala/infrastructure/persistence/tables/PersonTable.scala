package infrastructure.persistence.tables

import infrastructure.persistence.entities.PersonRow
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

class PersonTable(tag: Tag) extends Table[PersonRow](tag, "PERSONS"){

  def id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name: Rep[String] = column[String]("NAME")
  def age: Rep[Int] = column[Int]("AGE")

  def * : ProvenShape[PersonRow] = (id.?, name, age) <> (PersonRow.tupled, PersonRow.unapply)
}

object PersonTable {
  val table = TableQuery[PersonTable]
}
