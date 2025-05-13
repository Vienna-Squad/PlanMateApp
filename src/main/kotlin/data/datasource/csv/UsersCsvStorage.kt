package data.datasource.csv

import org.example.common.bases.CsvFileManager
import org.example.common.bases.DataSource
import org.example.data.datasource.csv.manager.UsersCsvFileManager
import org.example.domain.NoUsersFoundException
import org.example.domain.UserNotFoundException
import org.example.domain.entity.User
import java.util.*

class UsersCsvStorage(
    private val fileManager: CsvFileManager<User> = UsersCsvFileManager(),
) : DataSource<User> {
    override fun getAllItems() = fileManager.readAll().ifEmpty { throw NoUsersFoundException() }
    override fun addItem(newItem: User) = fileManager.append(newItem)
    override fun deleteItem(item: User) = fileManager.delete(item)
    override fun getItemById(id: UUID) = fileManager.getById(id) ?: throw UserNotFoundException()
    override fun updateItem(updatedItem: User) =
        fileManager.update(updatedItem).let { if (!it) throw UserNotFoundException() }
}