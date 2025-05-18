package org.example.data.datasource.local.csv.parser

import org.example.data.utils.Parser
import org.example.domain.entity.User
import java.time.LocalDateTime
import java.util.UUID

class UserCsvParser : Parser<String, User> {
    override fun serialize(item: User): String {
        return "${item.id},${item.username},${item.hashedPassword},${item.role},${item.cratedAt}\n"
    }

    override fun deserialize(item: String): User {
        val fields: List<String> = item.split(",")
        require(fields.size == EXPECTED_COLUMNS) { "Invalid user data format: " }
        val user = User(
            id = UUID.fromString(fields[ID_INDEX]),
            username = fields[USERNAME_INDEX],
            hashedPassword = fields[PASSWORD_INDEX],
            role = User.UserRole.valueOf(fields[TYPE_INDEX]),
            cratedAt = LocalDateTime.parse(fields[CREATED_AT_INDEX])
        )
        return user
    }

    companion object {
        private const val ID_INDEX = 0
        private const val USERNAME_INDEX = 1
        private const val PASSWORD_INDEX = 2
        private const val TYPE_INDEX = 3
        private const val CREATED_AT_INDEX = 4
        private const val EXPECTED_COLUMNS = 5
    }
}