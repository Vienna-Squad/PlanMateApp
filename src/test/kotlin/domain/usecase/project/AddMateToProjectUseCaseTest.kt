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
import java.util.*

class AddMateToProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase

    private val projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val mateId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
    private val username = "admin1"

    private val adminUser = User(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
        username = username,
        hashedPassword = "pass1",
        type = UserType.ADMIN,
        cratedAt = LocalDateTime.now()
    )

    private val mateUser = User(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
        username = "mate",
        hashedPassword = "pass2",
        type = UserType.MATE,
        cratedAt = LocalDateTime.now()
    )
    private val project = Project(
        id = projectId,
        name = "Project 1",
        states = listOf("ToDo", "InProgress"),
        createdBy =adminUser.id,
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
    fun `should throw NoFoundException when project does not exist`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.failure(NotFoundException(""))

        // When && Then
        assertThrows<NotFoundException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw AlreadyExistException when mate is already in project`() {
        // Given
        val projectWithMate = project.copy(matesIds = listOf(mateId))
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(projectWithMate)

        // When && Then
        assertThrows<AlreadyExistException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }



    @Test
    fun `should throw FailedToLogException when logging action fails`() {
        // Given
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { projectsRepository.updateProject(updatedProject) } returns Result.success(Unit)
        every { logsRepository.addLog(any()) } returns Result.failure(Exception("Log failed"))

        // When & Then
        assertThrows<FailedToLogException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is not the owner of the project`() {
        // Given
        val notOwnerAdmin = adminUser.copy(id = UUID.randomUUID())
        val projectCreatedByAnotherUser = project.copy(createdBy = UUID.randomUUID())

        every { authenticationRepository.getCurrentUser() } returns Result.success(notOwnerAdmin)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(projectCreatedByAnotherUser)

        // When & Then
        val exception = assertThrows<AccessDeniedException> {
            addMateToProjectUseCase(projectId, mateId)
        }
        
        assert(exception.message?.contains("You are not the owner of this project") == true)
    }



    @Test
    fun `should add mate to project and log the action when user is authorized`() {
        // Given
        val updatedProject = project.copy(matesIds = project.matesIds + mateId)

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)


        // When
        addMateToProjectUseCase(projectId, mateId)

        // Then
        verify { projectsRepository.updateProject(updatedProject) }
        verify { logsRepository.addLog(any()) }
}}