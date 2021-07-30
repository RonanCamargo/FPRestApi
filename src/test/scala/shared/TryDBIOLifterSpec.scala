package shared

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import shared.typeclasses.DBIOLifter
import slick.dbio.DBIO

import scala.util.Try

class TryDBIOLifterSpec
    extends AnyWordSpec
    with Matchers
    with BaseDBIOLifterSpec {

  val tryLifter: DBIOLifter[Try] = shared.instances.DBIOLifters.TryToDBIO

  "tryLifter" when {
    testLifter(tryLifter, Try(1), DBIO.successful(1))
  }

}
