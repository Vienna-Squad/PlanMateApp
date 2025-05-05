// src/main/kotlin/org/example/data/datasource/mongo/UsersMongoStorage.kt
package org.example.data.datasource.mongo

import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.time.LocalDateTime
import java.util.*


class UsersMongoStorage : MongoStorage<User>(MongoConfig.database.getCollection("User")) {

    override fun toDocument(item: User): Document {
        // Use string representation of UUID for _id field to avoid ObjectId conversion issues
        return Document()
            .append("_id", item.id.toString())
            .append("uuid", item.id.toString())  // Store UUID as string
            .append("username", item.username)
            .append("hashedPassword", item.hashedPassword)
            .append("role", item.role.name)
            .append("createdAt", item.cratedAt.toString())
    }

    override fun fromDocument(document: Document): User {
        // Use the "uuid" field to get the UUID string, then convert to UUID
        val uuidStr = document.getString("uuid") ?: document.getString("_id")

        return User(
            id = UUID.fromString(uuidStr),
            username = document.getString("username"),
            hashedPassword = document.getString("hashedPassword"),
            role = UserRole.valueOf(document.getString("role")),
            cratedAt = LocalDateTime.parse(document.getString("createdAt"))
        )
    }

    fun findByUsername(username: String): User? {
        val document = collection.find(Filters.eq("username", username)).first()
        return document?.let { fromDocument(it) }
    }
}