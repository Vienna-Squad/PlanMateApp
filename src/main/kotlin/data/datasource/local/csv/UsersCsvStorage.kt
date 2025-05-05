package data.datasource.local.csv

import org.example.data.datasource.local.csv.CsvStorage
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.io.File
import java.time.LocalDateTime
import java.util.*

class UsersCsvStorage(file: File) : CsvStorage<User>(file) {

    init {
        writeHeader(getHeaderString())
    }

    override fun toCsvRow(item: User): String {
        return "${item.id},${item.username},${item.hashedPassword},${item.role},${item.cratedAt}\n"
    }

    override fun fromCsvRow(fields: List<String>): User {
        require(fields.size == EXPECTED_COLUMNS) { "Invalid user data format: " }
        val user = User(
            id = UUID.fromString(fields[ID_INDEX]),
            username = fields[USERNAME_INDEX],
            hashedPassword = fields[PASSWORD_INDEX],
            role = UserRole.valueOf(fields[TYPE_INDEX]),
            cratedAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
        )
        return user
    }

    override fun getHeaderString(): String {
        return CSV_HEADER
    }

    override fun update(item: User) {
        if (!file.exists()) throw NotFoundException("file")
        val list = getAll().toMutableList()
        val itemIndex = list.indexOfFirst { it.id == item.id }
        if (itemIndex == -1) throw NotFoundException("$item")
        list[itemIndex] = item
        write(list)
    }

    override fun delete(item: User) {
        if (!file.exists()) throw NotFoundException("file")
        val list = getAll().toMutableList()
        val itemIndex = list.indexOfFirst { it.id == item.id }
        if (itemIndex == -1) throw NotFoundException("$item")
        list.removeAt(itemIndex)
        write(list)
    }

    companion object {
        const val CSV_HEADER = "id,username,password,type,createdAt\n"
        private const val ID_INDEX = 0
        private const val USERNAME_INDEX = 1
        private const val PASSWORD_INDEX = 2
        private const val TYPE_INDEX = 3
        private const val CREATED_AT_INDEX = 4
        private const val EXPECTED_COLUMNS = 5
    }
}