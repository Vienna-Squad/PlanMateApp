package data.storage

import org.example.domain.entity.User
import org.example.domain.entity.UserType
import java.time.LocalDateTime

class UserCsvStorage(filePath: String) : CsvStorage<User>(filePath) {

    companion object {
        // CSV column indices
        private const val ID_INDEX = 0
        private const val USERNAME_INDEX = 1
        private const val PASSWORD_INDEX = 2
        private const val TYPE_INDEX = 3
        private const val CREATED_AT_INDEX = 4

        private const val EXPECTED_COLUMNS = 5

        private const val CSV_HEADER = "id,username,password,type,createdAt"
    }

    override fun writeHeader() {
        writeToFile("$CSV_HEADER\n")
    }

    override fun serialize(item: User): String {
        return "${item.id},${item.username},${item.password},${item.type},${item.cratedAt}"
    }

    override fun deserialize(line: String): User {
        val parts = line.split(",", limit = EXPECTED_COLUMNS)
        require(parts.size == EXPECTED_COLUMNS) { "Invalid user data format: $line" }

        return User(
            id = parts[ID_INDEX],
            username = parts[USERNAME_INDEX],
            password = parts[PASSWORD_INDEX],
            type = UserType.valueOf(parts[TYPE_INDEX]),
            cratedAt = LocalDateTime.parse(parts[CREATED_AT_INDEX])
        )
    }
}