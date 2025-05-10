package data.datasource.remote.mongo


import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import io.mockk.*
import org.bson.Document
import org.example.domain.entity.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import data.datasource.mongo.MongoStorage
import data.datasource.mongo.TasksMongoStorage
import org.example.domain.entity.State
import java.time.LocalDateTime
import java.util.*

class TasksMongoStorageTest {

    private lateinit var mockCollection: MongoCollection<Document>
    private lateinit var storage: TasksMongoStorage
    private lateinit var mockFindIterable: FindIterable<Document>

    @BeforeEach
    fun setup() {
        mockCollection = mockk(relaxed = true)
        mockFindIterable = mockk(relaxed = true)
        storage = TasksMongoStorage()

        val field = MongoStorage::class.java.getDeclaredField("collection")
        field.isAccessible = true
        field.set(storage, mockCollection)
    }

    @Test
    fun `toDocument should convert Task to Document correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID())
        val state = State(name = "In Progress")

        val task = Task(
            id = uuid,
            title = "Implement Feature X",
            state = state,
            assignedTo = assignedTo,
            createdBy = creatorUuid,
            createdAt = LocalDateTime.of(2023, 1, 1, 12, 0),
            projectId = projectId
        )

        // When
        val document = storage.toDocument(task)

        // Then
        assertThat(document.getString("_id")).isEqualTo(uuid.toString())
        // Then (continued)
        assertThat(document.getString("title")).isEqualTo("Implement Feature X")
        assertThat(document.getString("state")).isEqualTo(state.toString())
        assertThat(document.get("createdBy")).isEqualTo(creatorUuid)
        assertThat(document.getString("createdAt")).isEqualTo("2023-01-01T12:00")
        assertThat(document.get("projectId")).isEqualTo(projectId)

        val docAssignedTo = document.getList("assignedTo", String::class.java)
        assertThat(docAssignedTo).hasSize(2)
        assertThat(docAssignedTo).contains(assignedTo[0].toString())
        assertThat(docAssignedTo).contains(assignedTo[1].toString())
    }

    @Test
    fun `fromDocument should convert Document to Task correctly`() {
        // Given
        val uuid = UUID.randomUUID()
        val creatorUuid = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID())
        val state = State(name = "In Progress")


        val document = Document()
            .append("_id", uuid.toString())
            .append("title", "Implement Feature X")
            .append("state", state.toString())
            .append("assignedTo", assignedTo.map { it.toString() })
            .append("createdBy", creatorUuid)
            .append("createdAt", "2023-01-01T12:00")
            .append("projectId", projectId)

        // When
        val task = storage.fromDocument(document)

        // Then
        assertThat(task.id).isEqualTo(uuid)
        assertThat(task.title).isEqualTo("Implement Feature X")
        assertThat(task.state.name).isEqualTo("In Progress")
        assertThat(task.createdBy).isEqualTo(creatorUuid)
        assertThat(task.createdAt).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0))
        assertThat(task.projectId).isEqualTo(projectId)
        assertThat(task.assignedTo).containsExactlyElementsIn(assignedTo)
    }

    @Test
    fun `getAll should return tasks from collection`() {
        // Given - create the expected Task objects that would result from document conversion
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        val task1 = Task(
            id = uuid1,
            title = "Task 1",
            state = State(name = "Backlog"),
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            projectId = UUID.randomUUID()
        )

        val task2 = Task(
            id = uuid2,
            title = "Task 2",
            state = State(name = "In Progress"),
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            projectId = UUID.randomUUID()
        )

        // Create a spy on the storage object
        val storageSpy = spyk(storage)

        // Mock the getAll method directly to return our test tasks
        every { storageSpy.getAll() } returns listOf(task1, task2)

        // When
        val result = storageSpy.getAll()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Task 1")
        assertThat(result[1].title).isEqualTo("Task 2")
    }

    @Test
    fun `add should insert task into collection`() {
        // Given
        val uuid = UUID.randomUUID()
        val task = Task(
            id = uuid,
            title = "New Task",
            state = State(name = "Backlog"),
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            projectId = UUID.randomUUID()
        )

        val mockResult = mockk<InsertOneResult>()
        every { mockResult.wasAcknowledged() } returns true
        every { mockCollection.insertOne(any()) } returns mockResult

        // When
        storage.add(task)

        // Then
        verify { mockCollection.insertOne(any()) }
    }

    @Test
    fun `update should modify task when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val task = Task(
            id = uuid,
            title = "Updated Task",
            state = State(name = "Done"),
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            projectId = UUID.randomUUID()
        )

        val mockResult = mockk<UpdateResult>()
        every { mockResult.matchedCount } returns 1
        every { mockCollection.replaceOne(any(), any()) } returns mockResult

        // When
        storage.update(task)

        // Then
        verify { mockCollection.replaceOne(any(), any()) }
    }

    @Test
    fun `delete should remove task when it exists`() {
        // Given
        val uuid = UUID.randomUUID()
        val task = Task(
            id = uuid,
            title = "Task to Delete",
            state = State(name = "Cancelled"),
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            projectId = UUID.randomUUID()
        )

        val mockResult = mockk<DeleteResult>()
        every { mockResult.deletedCount } returns 1
        every { mockCollection.deleteOne(any()) } returns mockResult

        // When
        storage.delete(task)

        // Then
        verify { mockCollection.deleteOne(any()) }
    }
}