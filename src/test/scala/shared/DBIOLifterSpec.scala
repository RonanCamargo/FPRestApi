package shared

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import slick.dbio.DBIO

import scala.util.{Failure, Try}

class DBIOLifterSpec extends AnyWordSpec with Matchers {

  "#liftToDBIO" when {
    import shared.syntax.DBIOSyntax._

    "calling extension method for Either" should {
      import shared.instances.DBIOLifters.EitherToDBIO

      "return a successful DBIO if it's a Right" in {
        val either: Either[Throwable, Int] = Right(1)
        val expected: DBIO[Int]            = DBIO.successful(1)

        val result: DBIO[Int] = either.liftToDBIO

        result shouldBe expected
      }

      "return a failed DBIO if it's a Left" in {
        val exception                      = new RuntimeException
        val either: Either[Throwable, Int] = Left(exception)
        val expected: DBIO[Int]            = DBIO.failed(exception)

        val result: DBIO[Int] = either.liftToDBIO

        result shouldBe expected

      }
    }

    "calling extension method for Try" should {
      import shared.instances.DBIOLifters.TryToDBIO
      "return a successful DBIO if it's a Success" in {
        val _try: Try[Int]      = Try(1)
        val expected: DBIO[Int] = DBIO.successful(1)

        val result: DBIO[Int] = _try.liftToDBIO

        result shouldBe expected
      }

      "return a failed DBIO if it's a Failure" in {
        val exception           = new RuntimeException
        val _try: Try[Int]      = Failure(exception)
        val expected: DBIO[Int] = DBIO.failed(exception)

        val result: DBIO[Int] = _try.liftToDBIO

        result shouldBe expected
      }

    }
  }

}
