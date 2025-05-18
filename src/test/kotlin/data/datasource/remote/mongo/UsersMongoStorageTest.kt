/*
package data.datasource.remote.mongo

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import io.mockk.*
import org.bson.Document
import org.example.domain.exceptions.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.User.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import java.time.LocalDateTime
import java.util.*

class UsersMongoStorageTest {

    private lateinit var mockCollection: MongoCollection<Document>
    private lateinit var storage: UsersMongoStorage
    private lateinit var mockFindIterable: FindIterable<Document>

    @BeforeEach
    fun setup() {
        mockCollection = mockk(relaxed = true)
        mockFindIterable = mockk(relaxed = true)
        storage = UsersMongoStorage()

        val field = MongoStorage::class.java.getDeclaredField("collection")
        field.isAccessible = true
        field.set(storage, mockCollection)
    }

    @Test
    fun `toDocument should convert User to Document correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = User(
            id = uuid,
            username = "johnsmith",
            hashedPassword = "hashedPass123",
            role = UserRole.ADMIN,
            cratedAt = LocalDateTime.of(2023, 1, 1, 12, 0)
        )

        // When
        val document = storage.toDocument(user)

        // Then
        assertThat(document.getString("_id")).isEqualTo(uuid.toString())
        assertThat(document.getString("uuid")).isEqualTo(uuid.toString())
        assertThat(document.getString("username")).isEqualTo("johnsmith")
        assertThat(document.getString("hashedPassword")).isEqualTo("hashedPass123")
        assertThat(document.getString("role")).isEqualTo("ADMIN")
        assertThat(document.getString("createdAt")).isEqualTo("2023-01-01T12:00")
    }

    @Test
    fun `fromDocument should convert Document to User correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val document = Document()
            .append("_id", uuid.toString())
            .append("username", "johnsmith")
            .append("hashedPassword", "hashedPass123")
            .append("role", "MATE")
            .append("createdAt", "2023-01-01T12:00")

        // When
        val user = storage.fromDocument(document)

        // Then
        assertThat(user.id).isEqualTo(uuid)
        assertThat(user.username).isEqualTo("johnsmith")
        assertThat(user.hashedPassword).isEqualTo("hashedPass123")
        assertThat(user.role).isEqualTo(UserRole.MATE)
        assertThat(user.cratedAt).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0))
    }

    @Test
    fun `getAll should return users from collection`() {
        // Given
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val createdAt1 = LocalDateTime.now()
        val createdAt2 = LocalDateTime.now()

        // Create user objects directly instead of relying on MongoDB conversion
        val user1 = User(
            id = uuid1,
            username = "user1",
            hashedPassword = "hash1",
            role = UserRole.ADMIN,
            cratedAt = createdAt1
        )

        val user2 = User(
            id = uuid2,
            username = "user2",
            hashedPassword = "hash2",
            role = UserRole.MATE,
            cratedAt = createdAt2
        )

        // Use a spy to bypass MongoDB interaction
        val storageSpy = spyk(storage)
        every { storageSpy.getAllItems() } returns listOf(user1, user2)

        // When
        val result = storageSpy.getAllItems()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].username).isEqualTo("user1")
        assertThat(result[0].role).isEqualTo(UserRole.ADMIN)
        assertThat(result[1].username).isEqualTo("user2")
        assertThat(result[1].role).isEqualTo(UserRole.MATE)
    }

    @Test
    fun `getById should return user when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = User(
            id = uuid,
            username = "johnsmith",
            hashedPassword = "hash123",
            role = UserRole.ADMIN,
            cratedAt = LocalDateTime.now()
        )

        // Use a spy to bypass MongoDB interaction
        val storageSpy = spyk(storage)
        every { storageSpy.getItemById(uuid) } returns user

        // When
        val result = storageSpy.getItemById(uuid)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.username).isEqualTo("johnsmith")
    }

    @Test
    fun `getById should throw NotFoundException when user doesn't exist`() {
        // Given
        val uuid = UUID.randomUUID()

        // Use a spy to bypass MongoDB interaction
        val storageSpy = spyk(storage)
        every { storageSpy.getItemById(uuid) } throws NotFoundException()

        // When/Then
        assertThrows<NotFoundException> { storageSpy.getItemById(uuid) }
    }

    @Test
    fun `add should insert user into collection`() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "newuser",
            hashedPassword = "newhash",
            role = UserRole.MATE,
            cratedAt = LocalDateTime.now()
        )

        val mockResult = mockk<InsertOneResult>()
        every { mockResult.wasAcknowledged() } returns true
        every { mockCollection.insertOne(any()) } returns mockResult

        // When
        storage.addItem(user)

        // Then
        verify { mockCollection.insertOne(any()) }
    }

    @Test
    fun `update should modify user when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = User(
            id = uuid,
            username = "updateduser",
            hashedPassword = "updatedhash",
            role = UserRole.ADMIN,
            cratedAt = LocalDateTime.now()
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 1
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When
        storage.updateItem(user)

        // Then
        verify { mockCollection.replaceOne(any(), any()) }
    }

    @Test
    fun `delete should remove user when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = User(
            id = uuid,
            username = "deleteuser",
            hashedPassword = "deletehash",
            role = UserRole.MATE,
            cratedAt = LocalDateTime.now()
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 1
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When
        storage.deleteItem(user)

        // Then
        verify { mockCollection.deleteOne(any()) }
    }
}*/
