package data.storage

import com.google.common.truth.Truth.assertThat
import org.example.data.datasource.csv.ProjectsCsvStorage
import org.example.domain.entity.Project
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*


class ProjectCsvStorageTest {
    private lateinit var tempFile: File
    private lateinit var storage: ProjectsCsvStorage

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFile = tempDir.resolve("projects_test.csv").toFile()
        storage = ProjectsCsvStorage(tempFile)
    }

    @Test
    fun `should create file with header when initialized`() {
        // Given - initialization in setUp

        // When - file creation happens in init block

        // Then
        assertThat(tempFile.exists()).isTrue()
        assertThat(tempFile.readText()).contains("id,name,states,createdBy,matesIds,createdAt")
    }

    @Test
    fun `should correctly serialize and append a project`() {
        // Given
        val project = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Test Project",
            states = listOf("TODO", "In Progress", "Done"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            matesIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
        )

        // When
        storage.append(project)

        // Then
        val projects = storage.read()
        assertThat(projects).hasSize(1)

        val savedProject = projects[0]
        assertThat(savedProject.id).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
        assertThat(savedProject.name).isEqualTo("Test Project")
        assertThat(savedProject.states).containsExactly("TODO", "In Progress", "Done")
        assertThat(savedProject.createdBy).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
        assertThat(savedProject.matesIds).containsExactly(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
    }

    @Test
    fun `should handle project with empty states and matesIds`() {
        // Given
        val project = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Empty Project",
            states = emptyList(),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            matesIds = emptyList()
        )

        // When
        storage.append(project)

        // Then
        val projects = storage.read()
        assertThat(projects).hasSize(1)

        val savedProject = projects[0]
        assertThat(savedProject.states).isEmpty()
        assertThat(savedProject.matesIds).isEmpty()
    }

    @Test
    fun `should handle multiple projects`() {
        // Given
        val project1 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
            name = "Project 1",
            states = listOf("TODO", "Done"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440005"),
            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            matesIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440006"))
        )

        val project2 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440007"),
            name = "Project 2",
            states = listOf("Backlog", "In Progress", "Testing", "Released"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440008"),
            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            matesIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440009"), UUID.fromString("550e8400-e29b-41d4-a716-446655440010"))
        )

        // When
        storage.append(project1)
        storage.append(project2)

        // Then
        val projects = storage.read()
        assertThat(projects).hasSize(2)
        assertThat(projects.map { it.id }).containsExactly(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"), UUID.fromString("550e8400-e29b-41d4-a716-446655440007"))
    }

    @Test
    fun `should correctly write a list of projects`() {
        // Given
        val project1 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
            name = "Project 1",
            states = listOf("TODO", "Done"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440005"),
            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            matesIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440006"))
        )

        val project2 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440007"),
            name = "Project 2",
            states = listOf("Backlog", "Released"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440008"),
            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            matesIds = listOf(UUID.fromString("550e8400-e29b-41d4-a716-446655440009"), UUID.fromString("550e8400-e29b-41d4-a716-446655440010"))
        )
        // When
        storage.write(listOf(project1, project2))

        // Then
        val projects = storage.read()
        assertThat(projects).hasSize(2)
        assertThat(projects.map { it.name }).containsExactly("Project 1", "Project 2")
    }

    @Test
    fun `should overwrite existing content when using write`() {
        // Given
        val project1 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
            name = "Original Project",
            states = listOf("TODO"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440005"),
            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00"),
            matesIds = emptyList()
        )

        val project2 = Project(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440007"),
            name = "New Project",
            states = listOf("Backlog"),
            createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440008"),
            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00"),
            matesIds = emptyList()
        )
        // First add project1
        storage.append(project1)

        // When - overwrite with project2
        storage.write(listOf(project2))

        // Then
        val projects = storage.read()
        assertThat(projects).hasSize(1)
        assertThat(projects[0].id).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440007"))
        assertThat(projects[0].name).isEqualTo("New Project")
    }

    @Test
    fun `should handle reading from non-existent file`() {
        // Given
        val nonExistentFile = File("non_existent_file.csv")
        val invalidStorage = ProjectsCsvStorage(nonExistentFile)

        // Ensure the file doesn't exist before reading
        if (nonExistentFile.exists()) {
            nonExistentFile.delete()
        }

        // When/Then
        assertThrows<FileNotFoundException> { invalidStorage.read() }
    }

    @Test
    fun `should throw IllegalArgumentException when reading malformed CSV`() {
        // Given
        tempFile.writeText("id1,name1\n") // Missing columns

        // When/Then
        assertThrows<IllegalArgumentException> { storage.read() }
    }

    @Test
    fun `should return empty list when file has only header`() {
        // Given
        // Only header is written during initialization

        // When
        val projects = storage.read()

        // Then
        assertThat(projects).isEmpty()
    }
}