package data.storage.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.storage.TaskCsvStorage
import org.example.data.storage.repository.TasksRepositoryImpl
import org.example.domain.NotFoundException

import org.example.domain.entity.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class TasksRepositoryImplTest {
    private lateinit var repository: TasksRepositoryImpl
    private lateinit var storage: TaskCsvStorage

    private val task1 = Task(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        title = "Task 1",
        state = "ToDo",
        assignedTo = emptyList(),
        createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
        projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        createdAt = LocalDateTime.now()
    )

    private val task2 = Task(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
        title = "Task 2",
        state = "Done",
        assignedTo = emptyList(),
        createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
        projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        createdAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setup() {
        storage = mockk(relaxed = true)
        repository = TasksRepositoryImpl(storage)
    }

    @Test
    fun `should return task when get is called with valid id from multiple tasks`() {
        // Given
        every { storage.read() } returns listOf(task1, task2)

        // When
        val result = repository.getTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))

        // Then
        assertTrue(result.isSuccess)
        assertEquals(task2, result.getOrThrow())
    }

    @Test
    fun `should return failure when get is called with invalid id`() {
        // Given
        every { storage.read() } returns listOf(task1, task2)

        // When
        val result =repository.getTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when get fails to read`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result =  repository.getTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return list of tasks when getAll is called`() {
        // Given
        every { storage.read() } returns listOf(task1, task2)

        // When
        val result = repository.getAllTasks()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf(task1, task2), result.getOrThrow())
    }

    @Test
    fun `should return failure when getAll fails to read`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getAllTasks()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should add task successfully when add is called`() {
        // Given
        every { storage.read() } returns listOf(task1)

        // When
        val result = repository.addTask(task1)

        // Then
        assertTrue(result.isSuccess)
        verify { storage.append(task1) }
    }

    @Test
    fun `should return failure when add fails`() {
        // Given
        every { storage.append(task1) } throws NotFoundException("")

        // When
        val result = repository.addTask(task1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should update task successfully when update is called`() {
        // Given
        val updatedTask = task1.copy(title = "Updated Task")
        every { storage.read() } returns listOf(task1)

        // When
        val result = repository.updateTask(updatedTask)

        // Then
        assertTrue(result.isSuccess)
        verify { storage.write(listOf(updatedTask)) }
    }

    @Test
    fun `should return failure when update is called with non-existent task`() {
        // Given
        every { storage.read() } returns emptyList()

        // When
        val result = repository.updateTask(task1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when update fails`() {
        // Given
        every { storage.read() } returns listOf(task1)
        every { storage.write(any()) } throws NotFoundException("")

        // When
        val result = repository.updateTask(task1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should delete task successfully when delete is called`() {
        // Given
        every { storage.read() } returns listOf(task1)

        // When
        val result = repository.deleteTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isSuccess)
        verify { storage.write(emptyList()) }
    }

    @Test
    fun `should return failure when delete is called with non-existent task`() {
        // Given
        every { storage.read() } returns listOf(task1)

        // When
        val result =repository.deleteTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when delete fails`() {
        // Given
        every { storage.read() } returns listOf(task1)
        every { storage.write(any()) } throws NotFoundException("")

        // When
        val result =  repository.deleteTaskById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isFailure)
    }
}