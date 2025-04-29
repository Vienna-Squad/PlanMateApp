package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*
import org.example.domain.entity.Project
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddMateToProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase

    // Common test data
    private val projectId = "P1"
    private val mateId = "M1"
    private val username = "admin1"

    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        addMateToProjectUseCase = AddMateToProjectUseCase(projectsRepository, logsRepository)
    }

    @Test
    fun `should add mate to project and log the action`() {
        // Given
        val project = Project("Project 1", listOf("ToDo", "InProgress"), username, listOf())
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { projectsRepository.update(updatedProject) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.success(Unit)

        // When
         addMateToProjectUseCase(projectId, mateId, username)

        // Then
        verify { projectsRepository.update(updatedProject) }

    }

    @Test
    fun `should fail if project does not exist`() {
        // Given
        every { projectsRepository.get(projectId) } returns Result.failure(NoProjectFoundException())

        // When && Then
        assertThrows<NoProjectFoundException> {
            addMateToProjectUseCase(projectId, mateId, username)
        }
    }

    @Test
    fun `should fail if mate is already in project`() {
        // Given
        val project = Project("Project 1", listOf("ToDo", "InProgress"), username, listOf(mateId))
        every { projectsRepository.get(projectId) } returns Result.success(project)

        // When && Then
        assertThrows<MateAlreadyInProjectException> {
            addMateToProjectUseCase(projectId, mateId, username)
        }
    }

    @Test
    fun `should fail if projectId is blank`() {
        // Given
        val blankProjectId = ""
        every { projectsRepository.get(projectId) } returns Result.failure(InvalidProjectIdException())

        // When && Then
        assertThrows<InvalidProjectIdException> {
            addMateToProjectUseCase(blankProjectId, mateId, username)
        }
    }

    @Test
    fun `should fail if mateId is blank`() {
        // Given
        val blankMateId = ""
        every { projectsRepository.get(projectId) } returns Result.failure(InvalidMateIdException())

        // When && Then
        assertThrows<InvalidMateIdException> {
            addMateToProjectUseCase(projectId, blankMateId, username)
        }
    }
}