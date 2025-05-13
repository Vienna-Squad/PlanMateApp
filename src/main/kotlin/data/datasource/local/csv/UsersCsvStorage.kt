package data.datasource.local.csv

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.DataSource
import org.example.domain.NoUsersFoundException
import org.example.domain.UserNotFoundException
import org.example.domain.entity.User
import java.util.*

class UsersCsvStorage(
    private val fileManager: CsvFileManager<User>,
) : DataSource<User> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoUsersFoundException() }
    override fun addItem(newItem: User) = fileManager.append(newItem)
    override fun deleteItem(item: User) = fileManager.delete(item).let { if (!it) throw UserNotFoundException() }
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw UserNotFoundException()
    override fun updateItem(updatedItem: User) =
        fileManager.update(updatedItem).let { if (!it) throw UserNotFoundException() }
}