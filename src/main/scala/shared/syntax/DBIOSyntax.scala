package shared.syntax

import shared.typeclasses.DBIOLifter
import slick.dbio.DBIO

object DBIOSyntax {
  implicit class DBIOOps[F[_], A](fa: F[A]) {
    def liftToDBIO(implicit lifter: DBIOLifter[F]): DBIO[A] =
      lifter.liftToDBIO(fa)
  }
}
