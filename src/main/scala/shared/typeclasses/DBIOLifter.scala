package shared.typeclasses

import slick.dbio.DBIO

trait DBIOLifter[F[_]] {
  def liftToDBIO[A](fa: F[A]): DBIO[A]
}
