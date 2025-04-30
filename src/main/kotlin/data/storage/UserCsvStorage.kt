package data.storage

import org.example.data.storage.CsvStorage
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import java.io.File
import java.time.LocalDateTime

class UserCsvStorage(file: File) : CsvStorage<User>(file) {

    init {
        writeHeader()
    }

    override fun writeHeader() {
        //"id,username,password,type,createdAt\n"
    }

    override fun toCsvRow(item: User): String {
        return "${item.id},${item.username},${item.password},${item.type},${item.cratedAt}"
    }

    override fun fromCsvRow(fields: List<String>): User {
        require(fields.size == 5) { "Invalid user data format: " }
        val user = User(
            id = fields[0],
            username = fields[1],
            password = fields[2],
            type = UserType.valueOf(fields[3]),
            cratedAt = LocalDateTime.parse(fields[4])
        )
        return user
    }
}