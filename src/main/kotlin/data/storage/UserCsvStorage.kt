package data.storage

import org.example.domain.entity.User
import org.example.domain.entity.UserType
import java.time.LocalDateTime

class UserCsvStorage(filePath: String) : CsvStorage<User>(filePath) {

    override fun writeHeader() {
        writeToFile("id,username,password,type,createdAt\n")
    }

    override fun serialize(item: User): String {
        return "${item.id},${item.username},${item.password},${item.type},${item.cratedAt}"
    }

    override fun deserialize(line: String): User {
        val parts = line.split(",", limit = 5)
        require(parts.size == 5) { "Invalid user data format: $line" }

        val user = User(
            id = parts[0],
            username = parts[1],
            password = parts[2],
            type = UserType.valueOf(parts[3]),
            cratedAt = LocalDateTime.parse(parts[4])
        )

        return user
    }
}