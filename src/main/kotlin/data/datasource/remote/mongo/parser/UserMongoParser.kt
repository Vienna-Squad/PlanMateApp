package org.example.data.datasource.remote.mongo.parser

import org.bson.Document
import org.example.data.utils.Parser
import org.example.domain.entity.User
import java.time.LocalDateTime
import java.util.UUID

class UserMongoParser : Parser<Document, User> {
    override fun serialize(item: User): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("uuid", item.id.toString())  // Store UUID as String
            .append("username", item.username)
            .append("hashedPassword", item.hashedPassword)
            .append("role", item.role.name)
            .append("createdAt", item.cratedAt.toString())
    }

    override fun deserialize(item: Document): User {
        val uuidStr = item.getString("_id")
        return User(
            id = UUID.fromString(uuidStr),
            username = item.getString("username"),
            hashedPassword = item.getString("hashedPassword"),
            role = User.UserRole.valueOf(item.getString("role")),
            cratedAt = LocalDateTime.parse(item.getString("createdAt"))
        )
    }
}