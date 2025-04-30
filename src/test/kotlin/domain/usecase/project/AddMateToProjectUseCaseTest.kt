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

class AddMateToProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase

    private val projectId = "P1"
    private val mateId = "M1"
    private val username = "admin1"

    private val adminUser = User(
        id = "U1",
        username = username,
        password = "pass1",
        type = UserType.ADMIN
    )

    private val mateUser = User(
        id = "U1",
        username = username,
        password = "pass1",
        type = UserType.MATE
    )
    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        authenticationRepository= mockk(relaxed = true)
        addMateToProjectUseCase = AddMateToProjectUseCase(projectsRepository, logsRepository,authenticationRepository)
    }

    @Test
    fun `should add mate to project and log the action when user is authorized`() {
        // Given
        val project = Project(
            id = projectId,
            name = "Project 1",
            states = listOf("ToDo", "InProgress"),
            createdBy = username,
            matesIds = emptyList()
        )
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { projectsRepository.update(updatedProject) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.success(Unit)


        // When
        addMateToProjectUseCase(projectId, mateId)

        // Then
        verify { projectsRepository.update(updatedProject) }

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
    fun `should throw UnauthorizedException when getCurrentUser fails`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        // When && Then
        assertThrows<UnauthorizedException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }
    @Test
    fun `should throw NoFoundException when project does not exist`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(projectId) } returns Result.failure(NoFoundException())

        // When && Then
        assertThrows<NoFoundException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw AlreadyExistException when mate is already in project`() {
        // Given
        val project = Project(
            id = projectId,
            name = "Project 1",
            states = listOf("ToDo", "InProgress"),
            createdBy = username,
            matesIds = listOf(mateId)
        )
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(projectId) } returns Result.success(project)

        // When && Then
        assertThrows<AlreadyExistException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw InvalidIdException when projectId is blank`() {
        // Given
        val blankProjectId = ""
        every { projectsRepository.get(projectId) } returns Result.failure(InvalidIdException())

        // When && Then
        assertThrows<InvalidIdException> {
            addMateToProjectUseCase(blankProjectId, mateId)
        }
    }

    @Test
    fun `should throw InvalidIdException when mateId is blank`() {
        // Given
        val blankMateId = ""
        every { projectsRepository.get(projectId) } returns Result.failure(InvalidIdException())

        // When && Then
        assertThrows<InvalidIdException> {
            addMateToProjectUseCase(projectId, blankMateId)
        }
    }
    @Test
    fun `should throw RuntimeException when update project fails`() {
        // Given
        val project = Project(
            id = projectId,
            name = "Project 1",
            states = listOf("ToDo", "InProgress"),
            createdBy = username,
            matesIds = emptyList()
        )
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { projectsRepository.update(updatedProject) } returns Result.failure(Exception("Update failed"))

        // When & Then
        assertThrows<RuntimeException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }

    @Test
    fun `should throw RuntimeException when logging action fails`() {
        // Given
        val project = Project(
            id = projectId,
            name = "Project 1",
            states = listOf("ToDo", "InProgress"),
            createdBy = username,
            matesIds = emptyList()
        )
        val updatedProject = project.copy(matesIds = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { projectsRepository.update(updatedProject) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.failure(Exception("Log failed"))

        // When & Then
        assertThrows<RuntimeException> {
            addMateToProjectUseCase(projectId, mateId)
        }
    }
}