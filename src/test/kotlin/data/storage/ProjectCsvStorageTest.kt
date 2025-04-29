package data.storage

import com.google.common.truth.Truth.assertThat
import data.TestUtils
import org.example.data.storage.ProjectCsvStorage
import org.example.domain.entity.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class ProjectCsvStorageTest {

    private lateinit var storage: ProjectCsvStorage
    private lateinit var tempFilePath: String

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFilePath = tempDir.resolve("projects_test.csv").toString()
        storage = ProjectCsvStorage(tempFilePath)
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
        assertThat(content).contains("id,name,states,createdBy,matesIds,createdAt")
    }

    @Test
    fun `should write and read projects correctly`() {
        // GIVEN
        val project1 = createTestProject(
            "Project 1",
            listOf("TODO", "In Progress", "Done"),
            "user1",
            listOf("user2", "user3")
        )

        val project2 = createTestProject(
            "Project 2",
            listOf("Backlog", "In Development", "Testing", "Released"),
            "user1",
            listOf("user4")
        )

        val projects = listOf(project1, project2)

        // WHEN
        storage.write(projects)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)

        val resultProject1 = result.find { it.name == "Project 1" }
        assertThat(resultProject1).isNotNull()
        assertThat(resultProject1!!.states).containsExactly("TODO", "In Progress", "Done")
        assertThat(resultProject1.createdBy).isEqualTo("user1")
        assertThat(resultProject1.matesIds).containsExactly("user2", "user3")

        val resultProject2 = result.find { it.name == "Project 2" }
        assertThat(resultProject2).isNotNull()
        assertThat(resultProject2!!.states).containsExactly("Backlog", "In Development", "Testing", "Released")
        assertThat(resultProject2.matesIds).containsExactly("user4")
    }

    @Test
    fun `should append project to existing file`() {
        // GIVEN
        val project1 = createTestProject("Project 1", listOf("TODO", "Done"), "user1", emptyList())
        storage.write(listOf(project1))

        val project2 = createTestProject("Project 2", listOf("Backlog", "Released"), "user1", listOf("user2"))

        // WHEN
        storage.append(project2)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Project 1", "Project 2")
    }

    @Test
    fun `should handle empty list when reading`() {
        // GIVEN - Empty file with just header

        // WHEN
        val result = storage.read()

        // THEN
        assertThat(result).isEmpty()
    }

    private fun createTestProject(
        name: String,
        states: List<String>,
        createdBy: String,
        matesIds: List<String>
    ): Project {
        return Project(name, states, createdBy, matesIds)
    }
}