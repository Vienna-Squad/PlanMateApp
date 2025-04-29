package org.example.data.storage

import org.example.domain.entity.User

class UserCsvStorage(filePath: String) : CsvStorage<User>(filePath) {
    override fun writeHeader() {
        TODO("Not yet implemented")
    }

    override fun serialize(item: User): String {
        TODO("Not yet implemented")
    }

    override fun deserialize(line: String): User {
        TODO("Not yet implemented")
    }
}