package org.example.data.datasource.remote.mongo

import org.bson.Document
import org.example.common.Constants.MongoCollections.USERS_COLLECTION
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.time.LocalDateTime
import java.util.*


class UsersMongoStorage : MongoStorage<User>(MongoConfig.database.getCollection(USERS_COLLECTION)) {
    override fun toDocument(item: User): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("uuid", item.id.toString())  // Store UUID as string
            .append("username", item.username)
            .append("hashedPassword", item.hashedPassword)
            .append("role", item.role.name)
            .append("createdAt", item.cratedAt.toString())
    }
    override fun fromDocument(document: Document): User {
        val uuidStr = document.getString("uuid") ?: document.getString("_id")
        return User(
            id = UUID.fromString(uuidStr),
            username = document.getString("username"),
            hashedPassword = document.getString("hashedPassword"),
            role = UserRole.valueOf(document.getString("role")),
            cratedAt = LocalDateTime.parse(document.getString("createdAt"))
        )
    }
}