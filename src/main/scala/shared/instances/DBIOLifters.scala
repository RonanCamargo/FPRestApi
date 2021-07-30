package shared.instances

import shared.typeclasses.DBIOLifter
import slick.dbio.DBIO

import scala.util.{Failure, Success, Try}

object DBIOLifters {
  type ErrorOr[A] = Either[Throwable, A]
  implicit val EitherToDBIO: DBIOLifter[ErrorOr] = new DBIOLifter[ErrorOr] {
    override def liftToDBIO[A](fa: ErrorOr[A]): DBIO[A] =
      fa match {
        case Right(value) => DBIO.successful(value)
        case Left(e)      => DBIO.failed(e)
      }
  }

  implicit val OptionToDBIO: DBIOLifter[Option] = new DBIOLifter[Option] {
    override def liftToDBIO[A](fa: Option[A]): DBIO[A] =
      fa match {
        case Some(value) => DBIO.successful(value)
        case None        => DBIO.failed(new RuntimeException)
      }
  }

  implicit val TryToDBIO: DBIOLifter[Try] = new DBIOLifter[Try] {
    override def liftToDBIO[A](fa: Try[A]): DBIO[A] =
      fa match {
        case Success(value)     => DBIO.successful(value)
        case Failure(exception) => DBIO.failed(exception)
      }
  }
}
