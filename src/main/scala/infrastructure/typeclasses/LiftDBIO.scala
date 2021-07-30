package infrastructure.typeclasses

import infrastructure.typeclasses.Lifters.ApplicationError
import slick.dbio.DBIO

trait LiftDBIO[F[_], A] {

  def liftToDBIO: DBIO[A]
}

trait LiftDBIO2[F[_]] {
  def liftToDBIO2[A](f: F[A]): DBIO[A]
}

object Lifters {
  type ApplicationError[A] = Either[Throwable, A]
//  implicit val eitherToDBIO = new LiftDBIO[ApplicationError] {
//    override def liftToDBIO[A] =
//  }

  implicit class EitherToDBIO[A](either: ApplicationError[A])
      extends LiftDBIO[ApplicationError, A] {
    override def liftToDBIO: DBIO[A] =
      either match {
        case Right(value) => DBIO.successful(value)
        case Left(e)      => DBIO.failed(e)
      }
  }

  implicit class OptionToDBIO[A](option: Option[A])
      extends LiftDBIO[ApplicationError, A] {
    override def liftToDBIO: DBIO[A] =
      option match {
        case Some(value) => DBIO.successful(value)
        case None        => DBIO.failed(new RuntimeException)
      }
  }

  val either: Either[Throwable, Int] = Right(1)
  val option: Option[Int]            = Some(1)

  val a: DBIO[Int] = either.liftToDBIO
  val b: DBIO[Int] = option.liftToDBIO

}

object Lifter2 {
  implicit val Dbio: LiftDBIO2[ApplicationError] =
    new LiftDBIO2[ApplicationError] {
      override def liftToDBIO2[A](f: ApplicationError[A]): DBIO[A] =
        f match {
          case Right(value) => DBIO.successful(value)
          case Left(e)      => DBIO.failed(e)
        }
    }

  val either: Either[Throwable, Int] = Right(1)

  implicit class DBIOLifter2[F[_], A](fa: F[A]) {
    def liftToDBIO2Syntax(implicit dbioLifter: LiftDBIO2[F]): DBIO[A] = dbioLifter.liftToDBIO2(fa)
  }

  either.liftToDBIO2Syntax

  def handle[F[_], A](a: F[A])(implicit dbioLifter: LiftDBIO2[F]): DBIO[A] = {
    dbioLifter.liftToDBIO2(a)
  }
}
