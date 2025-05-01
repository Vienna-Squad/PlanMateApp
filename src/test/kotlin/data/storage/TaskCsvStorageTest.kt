package data.storage

import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import org.example.data.storage.TaskCsvStorage
import org.example.domain.entity.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime

class TaskCsvStorageTest {
    private lateinit var tempFile: File
    private lateinit var storage: TaskCsvStorage

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFile = tempDir.resolve("tasks_test.csv").toFile()
        storage = TaskCsvStorage(tempFile)
    }

    @Test
    fun `should create file with header when initialized`() {
        // Given - initialization in setUp

        // When - file creation happens in init block

        // Then
        assertThat(tempFile.exists()).isTrue()
        assertThat(tempFile.readText()).contains("id,title,state,assignedTo,createdBy,projectId,createdAt")
    }

    @Test
    fun `should correctly serialize and append a task`() {
        // Given
        val task = Task(
            id = "task123",
            title = "Implement login feature",
            state = "In Progress",
            assignedTo = listOf("user1", "user2"),
            createdBy = "admin",
            createdAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            projectId = "proj123"
        )

        // When
        storage.append(task)

        // Then
        val tasks = storage.read()
        assertThat(tasks).hasSize(1)

        val savedTask = tasks[0]
        assertThat(savedTask.id).isEqualTo("task123")
        assertThat(savedTask.title).isEqualTo("Implement login feature")
        assertThat(savedTask.state).isEqualTo("In Progress")
        assertThat(savedTask.assignedTo).containsExactly("user1", "user2")
        assertThat(savedTask.createdBy).isEqualTo("admin")
        assertThat(savedTask.projectId).isEqualTo("proj123")
    }

    @Test
    fun `should handle task with empty assignedTo`() {
        // Given
        val task = Task(
            id = "task123",
            title = "Unassigned task",
            state = "TODO",
            assignedTo = emptyList(),
            createdBy = "admin",
            createdAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            projectId = "proj123"
        )

        // When
        storage.append(task)

        // Then
        val tasks = storage.read()
        assertThat(tasks).hasSize(1)

        val savedTask = tasks[0]
        assertThat(savedTask.assignedTo).isEmpty()
    }

    @Test
    fun `should handle multiple tasks`() {
        // Given
        val task1 = Task(
            id = "task1",
            title = "Task 1",
            state = "TODO",
            assignedTo = listOf("user1"),
            createdBy = "admin1",
            createdAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            projectId = "proj1"
        )

        val task2 = Task(
            id = "task2",
            title = "Task 2",
            state = "In Progress",
            assignedTo = listOf("user2", "user3"),
            createdBy = "admin2",
            createdAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            projectId = "proj1"
        )

        // When
        storage.append(task1)
        storage.append(task2)

        // Then
        val tasks = storage.read()
        assertThat(tasks).hasSize(2)
        assertThat(tasks.map { it.id }).containsExactly("task1", "task2")
    }

    @Test
    fun `should correctly write a list of tasks`() {
        // Given
        val task1 = Task(
            id = "task1",
            title = "Task 1",
            state = "TODO",
            assignedTo = listOf("user1"),
            createdBy = "admin1",
            createdAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            projectId = "proj1"
        )

        val task2 = Task(
            id = "task2",
            title = "Task 2",
            state = "In Progress",
            assignedTo = listOf("user2", "user3"),
            createdBy = "admin2",
            createdAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            projectId = "proj1"
        )

        // When
        storage.write(listOf(task1, task2))

        // Then
        val tasks = storage.read()
        assertThat(tasks).hasSize(2)
        assertThat(tasks.map { it.title }).containsExactly("Task 1", "Task 2")
    }

    @Test
    fun `should overwrite existing content when using write`() {
        // Given
        val task1 = Task(
            id = "task1",
            title = "Original Task",
            state = "TODO",
            assignedTo = emptyList(),
            createdBy = "admin1",
            createdAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            projectId = "proj1"
        )

        val task2 = Task(
            id = "task2",
            title = "New Task",
            state = "In Progress",
            assignedTo = emptyList(),
            createdBy = "admin2",
            createdAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            projectId = "proj1"
        )

        // First add task1
        storage.append(task1)

        // When - overwrite with task2
        storage.write(listOf(task2))

        // Then
        val tasks = storage.read()
        assertThat(tasks).hasSize(1)
        assertThat(tasks[0].id).isEqualTo("task2")
        assertThat(tasks[0].title).isEqualTo("New Task")
    }

    @Test
    fun `should handle reading from non-existent file`() {
        // Given
        val nonExistentFile = File("non_existent_file.csv")
        val invalidStorage = TaskCsvStorage(nonExistentFile)

        // When/Then
        assertThrows<FileNotFoundException> { invalidStorage.read() }
    }

    @Test
    fun `should throw IllegalArgumentException when reading malformed CSV`() {
        // Given
        tempFile.writeText("id1,title1,state1\n") // Missing columns

        // When/Then
        assertThrows<IllegalArgumentException> { storage.read() }
    }

    @Test
    fun `should return empty list when file has only header`() {
        // Given
        // Only header is written during initialization

        // When
        val tasks = storage.read()

        // Then
        assertThat(tasks).isEmpty()
    }
}