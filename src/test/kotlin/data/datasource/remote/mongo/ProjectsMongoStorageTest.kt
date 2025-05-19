package data.datasource.remote.mongo

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import io.mockk.*
import org.bson.Document
import org.example.domain.exceptions.NotFoundException
import org.example.domain.entity.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import dummyProject
import org.example.domain.entity.State
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
        // When
        val document = storage.toDocument(dummyProject)

        // Then
        assertThat(document.getString("_id")).isEqualTo(dummyProject.id.toString())
        assertThat(document.getString("name")).isEqualTo(dummyProject.name)
        assertThat(document.getList("states", String::class.java).size)
            .isEqualTo(dummyProject.states.size)
        assertThat(document.getString("createdBy")).isEqualTo(dummyProject.createdBy.toString())
        assertThat(document.getString("createdAt")).isEqualTo(dummyProject.createdAt.toString())

        val docMateIds = document.getList("matesIds", String::class.java)
        assertThat(docMateIds).hasSize(dummyProject.matesIds.size)
        assertThat(docMateIds).contains(dummyProject.matesIds[0].toString())
        assertThat(docMateIds).contains(dummyProject.matesIds[1].toString())
    }

    @Test
    fun `fromDocument should convert Document to Project correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val mateIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val states = listOf("Backlog", "In Progress", "Done").map { State(name = it) }

        val document = Document()
            .append("_id", uuid.toString())
            .append("name", "Test Project")
            .append("states", states.map { it.toString() })
            .append("createdBy", creatorUuid.toString())
            .append("createdAt", "2023-01-01T12:00")
            .append("matesIds", mateIds.map { it.toString() })

        // When
        val project = storage.fromDocument(document)

        // Then
        assertThat(project.id).isEqualTo(uuid)
        assertThat(project.name).isEqualTo("Test Project")
        assertThat(project.states.map { it.name }).containsExactly("Backlog", "In Progress", "Done")
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
            states = listOf("Backlog", "In Progress", "Done").map { State(name = it) },
            createdBy = creatorUuid,
            createdAt = LocalDateTime.parse("2023-01-01T12:00"),
            matesIds = document.getList("matesIds", String::class.java)
                .map { UUID.fromString(it) }
        )

        // Use a spy to intercept the call
        val spyStorage = spyk(storage)
        every { spyStorage.getItemById(uuid) } returns expectedProject

        // When
        val project = spyStorage.getItemById(uuid)

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
        every { spyStorage.getItemById(uuid) } throws NotFoundException()

        // When/Then
        assertThrows<NotFoundException> { spyStorage.getItemById(uuid) }
    }

    @Test
    fun `delete should remove project when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Test Project",
            states = listOf("Backlog", "In Progress", "Done").map { State(name = it) },
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 1
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When
        storage.deleteItem(project)

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
            states = listOf("Backlog", "In Progress", "Done").map { State(name = it) },
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 0
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When/Then
        assertThrows<NotFoundException> { storage.deleteItem(project) }
    }

    @Test
    fun `update should modify project when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val project = Project(
            id = uuid,
            name = "Updated Project",
            states = listOf("New", "Completed").map { State(name = it) },
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 1
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When
        storage.updateItem(project)

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
            states = listOf("New", "Completed").map { State(name = it) },
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            matesIds = listOf(UUID.randomUUID())
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 0
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When/Then
        assertThrows<NotFoundException> { storage.updateItem(project) }
    }
}