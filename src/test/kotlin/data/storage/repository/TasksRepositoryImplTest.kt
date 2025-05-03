//package data.storage.repository
//
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.example.data.storage.TaskCsvStorage
//import org.example.data.storage.repository.TasksRepositoryImpl
//import org.example.domain.NoFoundException
//import org.example.domain.entity.Task
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.BeforeEach
//import java.time.LocalDateTime
//import kotlin.test.Test
//
//class TasksRepositoryImplTest {
//    private lateinit var repository: TasksRepositoryImpl
//    private lateinit var storage: TaskCsvStorage
//
//    private val task1 = Task(
//        id = "T1",
//        title = "Task 1",
//        state = "ToDo",
//        assignedTo = emptyList(),
//        createdBy = "user1",
//        projectId = "P1",
//        createdAt = LocalDateTime.now()
//    )
//
//    private val task2 = Task(
//        id = "T2",
//        title = "Task 2",
//        state = "Done",
//        assignedTo = emptyList(),
//        createdBy = "user2",
//        projectId = "P1",
//        createdAt = LocalDateTime.now()
//    )
//
//    @BeforeEach
//    fun setup() {
//        storage = mockk(relaxed = true)
//        repository = TasksRepositoryImpl(storage)
//    }
//
//    @Test
//    fun `should return task when get is called with valid id from multiple tasks`() {
//        // Given
//        every { storage.read() } returns listOf(task1, task2)
//
//        // When
//        val result = repository.get("T2")
//
//        // Then
//        assertTrue(result.isSuccess)
//        assertEquals(task2, result.getOrThrow())
//    }
//
//    @Test
//    fun `should return failure when get is called with invalid id`() {
//        // Given
//        every { storage.read() } returns listOf(task1, task2)
//
//        // When
//        val result = repository.get("invalid_id")
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should return failure when get fails to read`() {
//        // Given
//        every { storage.read() } throws NoFoundException()
//
//        // When
//        val result = repository.get("T1")
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should return list of tasks when getAll is called`() {
//        // Given
//        every { storage.read() } returns listOf(task1, task2)
//
//        // When
//        val result = repository.getAll()
//
//        // Then
//        assertTrue(result.isSuccess)
//        assertEquals(listOf(task1, task2), result.getOrThrow())
//    }
//
//    @Test
//    fun `should return failure when getAll fails to read`() {
//        // Given
//        every { storage.read() } throws NoFoundException()
//
//        // When
//        val result = repository.getAll()
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should add task successfully when add is called`() {
//        // Given
//        every { storage.read() } returns listOf(task1)
//
//        // When
//        val result = repository.add(task1)
//
//        // Then
//        assertTrue(result.isSuccess)
//        verify { storage.append(task1) }
//    }
//
//    @Test
//    fun `should return failure when add fails`() {
//        // Given
//        every { storage.append(task1) } throws NoFoundException()
//
//        // When
//        val result = repository.add(task1)
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should update task successfully when update is called`() {
//        // Given
//        val updatedTask = task1.copy(title = "Updated Task")
//        every { storage.read() } returns listOf(task1)
//
//        // When
//        val result = repository.update(updatedTask)
//
//        // Then
//        assertTrue(result.isSuccess)
//        verify { storage.write(listOf(updatedTask)) }
//    }
//
//    @Test
//    fun `should return failure when update is called with non-existent task`() {
//        // Given
//        every { storage.read() } returns emptyList()
//
//        // When
//        val result = repository.update(task1)
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should return failure when update fails`() {
//        // Given
//        every { storage.read() } returns listOf(task1)
//        every { storage.write(any()) } throws NoFoundException()
//
//        // When
//        val result = repository.update(task1)
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should delete task successfully when delete is called`() {
//        // Given
//        every { storage.read() } returns listOf(task1)
//
//        // When
//        val result = repository.delete("T1")
//
//        // Then
//        assertTrue(result.isSuccess)
//        verify { storage.write(emptyList()) }
//    }
//
//    @Test
//    fun `should return failure when delete is called with non-existent task`() {
//        // Given
//        every { storage.read() } returns listOf(task1)
//
//        // When
//        val result = repository.delete("invalid_id")
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//
//    @Test
//    fun `should return failure when delete fails`() {
//        // Given
//        every { storage.read() } returns listOf(task1)
//        every { storage.write(any()) } throws NoFoundException()
//
//        // When
//        val result = repository.delete("T1")
//
//        // Then
//        assertTrue(result.isFailure)
//    }
//}