package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class AddMateToProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase

    private val projectId = UUID.randomUUID()
    private val mateId = UUID.randomUUID()
    private val username = "admin1"

    private val adminUser = User(
        id = UUID.randomUUID(),
        username = username,
        hashedPassword = "pass1",
        type = UserType.ADMIN,
        cratedAt = LocalDateTime.now()
    )

    private val mateUser = User(
        id = UUID.randomUUID(),
        username = "mate",
        hashedPassword = "pass2",
        type = UserType.MATE,
        cratedAt = LocalDateTime.now()
    )
    private val project = Project(
        id = projectId,
        name = "Project 1",
        states = listOf("ToDo", "InProgress"),
        createdBy = UUID.fromString(username),
        matesIds = emptyList(),
        cratedAt = LocalDateTime.now()
    )
    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        authenticationRepository= mockk(relaxed = true)
        addMateToProjectUseCase = AddMateToProjectUseCase(projectsRepository, logsRepository,authenticationRepository)
    }


    @Test
    fun `should throw UnauthorizedException when getCurrentUser fails`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

        // When && Then
        assertThrows<UnauthorizedException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is not authorized`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(mateUser)

        // When && Then
        assertThrows<AccessDeniedException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }


    @Test
    fun `should throw RuntimeException when update project fails`() {
        // Given
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { projectsRepository.updateProject(updatedProject) } returns Result.failure(Exception("Update failed"))

        // When & Then
        assertThrows<RuntimeException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw RuntimeException when logging action fails`() {
        // Given
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { projectsRepository.updateProject(updatedProject) } returns Result.success(Unit)
        every { logsRepository.addLog(any()) } returns Result.failure(Exception("Log failed"))

        // When & Then
        assertThrows<RuntimeException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should add mate to project and log the action when user is authorized`() {
        // Given
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)


        // When
        addMateToProjectUseCase(projectId, mateId)

        // Then
        verify { projectsRepository.updateProject(updatedProject) }
        verify { logsRepository.addLog(any()) }


    }
}