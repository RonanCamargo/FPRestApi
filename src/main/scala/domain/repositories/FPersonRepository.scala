package domain.repositories

trait FPersonRepository[F[_], Id, DBEntity] {

  def find(id: Id): F[Option[DBEntity]]

  def save(entity: DBEntity): F[DBEntity]

  def update(entity: DBEntity): F[Int]

  def delete(id: Id): F[Int]
}
