package data.datasource.remote.mongo

import org.example.data.datasource.DataSource
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.domain.entity.User
import org.example.domain.exceptions.AdditionException
import org.example.domain.exceptions.DeletionException
import org.example.domain.exceptions.ModificationException
import org.example.domain.exceptions.NoUsersFoundException
import org.example.domain.exceptions.UserNotFoundException
import java.util.*

class UsersMongoStorage(
    private val manager: MongoManager<User>
) : DataSource<User> {
    override fun getItemById(id: UUID) = manager.getById(id) ?: throw UserNotFoundException()

    override fun deleteItem(item: User) = manager.delete(item).let { if (!it) throw DeletionException() }

    override fun updateItem(updatedItem: User) =
        manager.update(updatedItem).let { if (!it) throw ModificationException() }

    override fun getAllItems() = manager.readAll().ifEmpty { throw NoUsersFoundException() }

    override fun addItem(newItem: User) = manager.append(newItem).let { if (!it) throw AdditionException() }
}