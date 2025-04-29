package data.storage

import com.google.common.truth.Truth.assertThat
import data.TestUtils
import org.example.data.storage.TaskCsvStorage
import org.example.domain.entity.Task
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class TaskCsvStorageTest {

    private lateinit var storage: TaskCsvStorage
    private lateinit var tempFilePath: String

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFilePath = tempDir.resolve("tasks_test.csv").toString()
        storage = TaskCsvStorage(tempFilePath)
    }

    @AfterEach
    fun tearDown() {
        TestUtils.cleanupFile(tempFilePath)
    }

    @Test
    fun `should create file with header when initialized`() {
        // WHEN - Storage is initialized in setUp

        // THEN - File should exist with header
        val content = java.io.File(tempFilePath).readText()
        assertThat(content).contains("id,title,state,assignedTo,createdBy,projectId,createdAt")
    }

    @Test
    fun `should write and read tasks correctly`() {
        // GIVEN
        val task1 = createTestTask(
            "Implement login",
            "In Progress",
            listOf("user2"),
            "user1",
            "project1"
        )

        val task2 = createTestTask(
            "Design UI",
            "TODO",
            listOf("user3", "user4"),
            "user1",
            "project1"
        )

        val tasks = listOf(task1, task2)

        // WHEN
        storage.write(tasks)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)

        val resultTask1 = result.find { it.title == "Implement login" }
        assertThat(resultTask1).isNotNull()
        assertThat(resultTask1!!.state).isEqualTo("In Progress")
        assertThat(resultTask1.assignedTo).containsExactly("user2")
        assertThat(resultTask1.createdBy).isEqualTo("user1")
        assertThat(resultTask1.projectId).isEqualTo("project1")

        val resultTask2 = result.find { it.title == "Design UI" }
        assertThat(resultTask2).isNotNull()
        assertThat(resultTask2!!.state).isEqualTo("TODO")
        assertThat(resultTask2.assignedTo).containsExactly("user3", "user4")
    }

    @Test
    fun `should append task to existing file`() {
        // GIVEN
        val task1 = createTestTask("Task 1", "TODO", emptyList(), "user1", "project1")
        storage.write(listOf(task1))

        val task2 = createTestTask("Task 2", "In Progress", listOf("user2"), "user1", "project1")

        // WHEN
        storage.append(task2)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)
        assertThat(result.map { it.title }).containsExactly("Task 1", "Task 2")
    }

    @Test
    fun `should handle empty list when reading`() {
        // GIVEN - Empty file with just header

        // WHEN
        val result = storage.read()

        // THEN
        assertThat(result).isEmpty()
    }

    private fun createTestTask(
        title: String,
        state: String,
        assignedTo: List<String>,
        createdBy: String,
        projectId: String
    ): Task {
        return Task(title, state, assignedTo, createdBy, projectId)
    }
}