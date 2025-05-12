package data.datasource.mongo

import org.bson.Document
import org.example.common.MongoCollections.USERS_COLLECTION
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import java.time.LocalDateTime
import java.util.*


class UsersMongoStorage : MongoStorage<User>(MongoConfig.database.getCollection(USERS_COLLECTION)) {
    override fun toDocument(item: User): Document {
        return Document()
            .append("_id", item.id.toString())
            .append("uuid", item.id.toString())  // Store UUID as String
            .append("username", item.username)
            .append("hashedPassword", item.hashedPassword)
            .append("role", item.role.name)
            .append("createdAt", item.cratedAt.toString())
    }

    override fun fromDocument(document: Document): User {
        val uuidStr = document.getString("_id")
        return User(
            id = UUID.fromString(uuidStr),
            username = document.getString("username"),
            hashedPassword = document.getString("hashedPassword"),
            role = User.UserRole.valueOf(document.getString("role")),
            cratedAt = LocalDateTime.parse(document.getString("createdAt"))
        )
    }

    override fun getAllItems() = super.getAllItems().ifEmpty { throw NotFoundException("users") }
}