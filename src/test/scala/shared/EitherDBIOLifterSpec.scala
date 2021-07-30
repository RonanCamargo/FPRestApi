package shared

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import shared.instances.DBIOLifters.ErrorOr
import shared.typeclasses.DBIOLifter
import slick.dbio.DBIO

class EitherDBIOLifterSpec
    extends AnyWordSpec
    with Matchers
    with BaseDBIOLifterSpec {

  "eitherLifter" when {
    val eitherLifter: DBIOLifter[ErrorOr] =
      shared.instances.DBIOLifters.EitherToDBIO

//    val rightInput: ErrorOr[Int]    = Right(1)
//    val successfulOutput: DBIO[Int] = DBIO.successful(1)

    testLifter(
      eitherLifter,
      Right[Throwable, Int](1),
      DBIO.successful(1)
    )

    val exception = new RuntimeException
//    val leftInput: ErrorOr[String] = Left(exception)
//    val failedOutput: DBIO[String] = DBIO.failed(exception)

    testLifter(
      eitherLifter,
      Left[Throwable, String](exception),
      DBIO.failed(exception)
    )

  }
}
