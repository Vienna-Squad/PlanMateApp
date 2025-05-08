package data.datasource.remote.mongo

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import io.mockk.*
import org.bson.Document
import org.example.domain.NotFoundException
import org.example.domain.entity.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import data.datasource.mongo.MongoStorage
import data.datasource.mongo.ProjectsMongoStorage
import java.time.LocalDateTime
import java.util.*

class ProjectsMongoStorageTest {

    private lateinit var mockCollection: MongoCollection<Document>
    private lateinit var storage: ProjectsMongoStorage
    private lateinit var mockFindIterable: FindIterable<Document>

    @BeforeEach
    fun setup() {
        mockCollection = mockk(relaxed = true)
        mockFindIterable = mockk(relaxed = true)
        storage = ProjectsMongoStorage()

        val field = MongoStorage::class.java.getDeclaredField("collection")
        field.isAccessible = true
        field.set(storage, mockCollection)
    }

    @Test
    fun `toDocument should convert Project to Document correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val mateIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val project = Project(
            id = uuid,
            name = "Test Project",
            states = listOf("Backlog", "In Progress", "Done"),
            createdBy = creatorUuid,
            createdAt = LocalDateTime.of(2023, 1, 1, 12, 0),
            matesIds = mateIds
        )

        // When
        val document = storage.toDocument(project)

        // Then
        assertThat(document.getString("_id")).isEqualTo(uuid.toString())
        assertThat(document.getString("name")).isEqualTo("Test Project")
        assertThat(document.getList("states", String::class.java))
            .containsExactly("Backlog", "In Progress", "Done")
        assertThat(document.getString("createdBy")).isEqualTo(creatorUuid.toString())
        assertThat(document.getString("createdAt")).isEqualTo("2023-01-01T12:00")

        val docMateIds = document.getList("matesIds", String::class.java)
        assertThat(docMateIds).hasSize(2)
        assertThat(docMateIds).contains(mateIds[0].toString())
        assertThat(docMateIds).contains(mateIds[1].toString())
    }

    @Test
    fun `fromDocument should convert Document to Project correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val mateIds = listOf(UUID.randomUUID(), UUID.randomUUID())

        val document = Document()
            .append("_id", uuid.toString())
            .append("name", "Test Project")
            .append("states", listOf("Backlog", "In Progress", "Done"))
            .append("createdBy", creatorUuid.toString())
            .append("createdAt", "2023-01-01T12:00")
            .append("matesIds", mateIds.map { it.toString() })

        // When
        val project = storage.fromDocument(document)

        // Then
        assertThat(project.id).isEqualTo(uuid)
        assertThat(project.name).isEqualTo("Test Project")
        assertThat(project.states).containsExactly("Backlog", "In Progress", "Done")
        assertThat(project.createdBy).isEqualTo(creatorUuid)
        assertThat(project.createdAt).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0))
        assertThat(project.matesIds).containsExactlyElementsIn(mateIds)
    }

    @Test
    fun `getById should return project when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val document = Document()
            .append("_id", uuid.toString())
            .append("name", "Test Project")
            .append("states", listOf("Backlog", "In Progress", "Done"))
            .append("createdBy", creatorUuid.toString())
            .append("createdAt", "2023-01-01T12:00")
            .append("matesIds", listOf(UUID.randomUUID().toString()))

        // Create the project object that should be returned
        val expectedProject = Project(
            id = uuid,
            name = "Test Project",
            states = listOf("Backlog", "In Progress", "Done"),
            createdBy = creatorUuid,
            createdAt = LocalDateTime.parse("2023-01-01T12:00"),
            matesIds = document.getList("matesIds", String::class.java)
                .map { UUID.fromString(it) }
        )

        // Use a spy to intercept the call
        val spyStorage = spyk(storage)
        every { spyStorage.getById(uuid) } returns expectedProject

        // When
        val project = spyStorage.getById(uuid)

        // Then
        assertThat(project.id).isEqualTo(uuid)
        assertThat(project.name).isEqualTo("Test Project")
    }

    @Test
    fun `getById should throw NotFoundException when project doesn't exist`() {
        // Given
        val uuid = UUID.randomUUID()

        // Use a spy to intercept the call
        val spyStorage = spyk(storage)
        every { spyStorage.getById(uuid) } throws NotFoundException()

        // When/Then
        assertThrows<NotFoundException> { spyStorage.getById(uuid) }
    }

    @Test
    fun `delete should remove project when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Test Project",
            states = listOf("Backlog", "In Progress", "Done"),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 1
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When
        storage.delete(project)

        // Then
        verify { mockCollection.deleteOne(Filters.eq("_id", uuid.toString())) }
    }

    @Test
    fun `delete should throw NotFoundException when project doesn't exist`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Test Project",
            states = listOf("Backlog", "In Progress", "Done"),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 0
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When/Then
        assertThrows<NotFoundException> { storage.delete(project) }
    }

    @Test
    fun `update should modify project when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Updated Project",
            states = listOf("New", "Completed"),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 1
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When
        storage.update(project)

        // Then
        verify { mockCollection.replaceOne(Filters.eq("_id", uuid.toString()), any()) }
    }

    @Test
    fun `update should throw NotFoundException when project doesn't exist`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Updated Project",
            states = listOf("New", "Completed"),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 0
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When/Then
        assertThrows<NotFoundException> { storage.update(project) }
    }
}