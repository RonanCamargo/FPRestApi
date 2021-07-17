package domain.repositories

/** Repository for CRUD operations
  * @tparam F Effect type
  * @tparam Id Identifier
  * @tparam E Entity
  */
trait Repository[F[_], Id, E] {

  def findAll: F[Seq[E]]

  def find(id: Id): F[Option[E]]

  def save(entity: E): F[E]

  def update(entity: E): F[Int]

  def delete(id: Id): F[Int]
}
