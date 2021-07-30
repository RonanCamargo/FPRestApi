package shared

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import shared.typeclasses.DBIOLifter
import slick.dbio.DBIO

trait BaseDBIOLifterSpec { self: AnyWordSpec with Matchers =>

  def testLifter[F[_], A](
      lifter: DBIOLifter[F],
      input: F[A],
      output: DBIO[A]
  ): Unit = {

    s"receiving ${input.toString} as input" should {
      s"return ${output.toString}" in {

        val result: DBIO[A] = lifter.liftToDBIO(input)
        result shouldBe output
      }
    }

    s"using an extension method and receiving ${input.toString} as input" should {
      import shared.syntax.DBIOSyntax._

      s"return ${output.toString}" in {

        val result: DBIO[A] = input.liftToDBIO(lifter)
        result shouldBe output
      }
    }

  }
}
