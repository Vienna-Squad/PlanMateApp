package data.storage.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.datasource.local.csv.ProjectsCsvStorage
import org.example.data.repository.ProjectsRepositoryImpl
import org.example.domain.NotFoundException

import org.example.domain.entity.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class ProjectsRepositoryImplTest {
    private lateinit var repository: ProjectsRepositoryImpl
    private lateinit var storage: ProjectsCsvStorage

    private val project1 = Project(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        name = "Project 1",
        states = listOf("ToDo", "InProgress"),
        createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
        matesIds = emptyList(),
        cratedAt = LocalDateTime.now()
    )

    private val project2 = Project(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        name = "Project 2",
        states = listOf("Done"),
        createdBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
        matesIds = emptyList(),
        cratedAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setup() {
        storage = mockk(relaxed = true)
        repository = ProjectsRepositoryImpl(storage)
    }

    @Test
    fun `should return project when get is called with valid id from multiple projects`() {
        // Given
        every { storage.read() } returns listOf(project1, project2)

        // When
        val result = repository.getProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))

        // Then
        assertTrue(result.isSuccess)
        assertEquals(project2, result.getOrThrow())
    }

    @Test
    fun `should return failure when get is called with invalid id`() {
        // Given
        every { storage.read() } returns listOf(project1, project2)

        // When
        val result = repository.getProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when get fails to read`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return list of projects when getAll is called`() {
        // Given
        every { storage.read() } returns listOf(project1, project2)

        // When
        val result = repository.getAllProjects()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf(project1, project2), result.getOrThrow())
    }

    @Test
    fun `should return failure when getAll fails to read`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getAllProjects()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should add project successfully when add is called`() {
        // Given
        every { storage.read() } returns listOf(project1)

        // When
        val result = repository.addProject(project1)

        // Then
        assertTrue(result.isSuccess)
        verify { storage.append(project1) }
    }

    @Test
    fun `should return failure when add fails`() {
        // Given
        every { storage.append(project1) } throws NotFoundException("")

        // When
        val result = repository.addProject(project1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should update project successfully when update is called`() {
        // Given
        val updatedProject = project1.copy(name = "Updated Project")
        every { storage.read() } returns listOf(project1)

        // When
        val result = repository.updateProject(updatedProject)

        // Then
        assertTrue(result.isSuccess)
        verify { storage.write(listOf(updatedProject)) }
    }

    @Test
    fun `should return failure when update is called with non-existent project`() {
        // Given
        every { storage.read() } returns emptyList()

        // When
        val result = repository.updateProject(project1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when update fails`() {
        // Given
        every { storage.read() } returns listOf(project1)
        every { storage.write(any()) } throws NotFoundException("")

        // When
        val result = repository.updateProject(project1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should delete project successfully when delete is called`() {
        // Given
        every { storage.read() } returns listOf(project1)

        // When
        val result = repository.deleteProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isSuccess)
        verify { storage.write(emptyList()) }
    }

    @Test
    fun `should return failure when delete is called with non-existent project`() {
        // Given
        every { storage.read() } returns listOf(project1)

        // When
        val result =repository.deleteProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when delete fails`() {
        // Given
        every { storage.read() } returns listOf(project1)
        every { storage.write(any()) } throws NotFoundException("")

        // When
        val result = repository.deleteProjectById(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isFailure)
    }
}