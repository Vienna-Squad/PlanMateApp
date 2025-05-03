package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*

import org.example.domain.entity.AddedLog
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AddStateToProjectUseCaseTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        addStateToProjectUseCase =
            AddStateToProjectUseCase(authenticationRepository, projectsRepository, logsRepository)

    }

    @Test
    fun `should throw UnauthorizedException when no logged-in user is found`() {
        //Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(Exception())
        // Then&&When
        assertThrows<UnauthorizedException> {
            addStateToProjectUseCase.invoke(
                projectId = UUID.fromString("non-existent project"),
                state = "New State"
            )
        }
    }

    @Test
    fun `should throw AccessDeniedException when attempting to add a state to project given current user is not admin`() {
        //Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(mate)
        // Then&&When
        assertThrows<AccessDeniedException> {
            addStateToProjectUseCase.invoke(
                projectId = projects[0].id,
                state = "New State"
            )
        }
    }

    @Test
    fun `should throw AccessDeniedException when attempting to add a state to project given current user non-related to project`() {
        //Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(mate)
        // Then&&When
        assertThrows<AccessDeniedException> {
            addStateToProjectUseCase.invoke(
                projectId = projects[1].id,
                state = "New State"
            )
        }
    }

    @Test
    fun `should throw NoFoundException when attempting to add a state to a non-existent project`() {
        //Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(admin)
        every { projectsRepository.getAllProjects() } returns Result.failure(NotFoundException("No project found"))
        // When & Then
        assertThrows< NotFoundException> {
            addStateToProjectUseCase.invoke(
                projectId = UUID.fromString("non-existent project"),
                state = "New State"
            )
        }

    }

    @Test

    fun `should throw DuplicateStateException state add log to logs given project id`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(admin)
        every { projectsRepository.getProjectById(any()) } returns Result.success(projects[0])
        // When
        //Then
        assertThrows<AlreadyExistException> {
            addStateToProjectUseCase(
                projectId = projects[0].id,
                state = "Done"
            )
        }
    }

    @Test

    fun `should throw FailedToLogException when fail to log `() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(admin)
        every { projectsRepository.getProjectById(any()) } returns Result.success(projects[0])
        every { logsRepository.addLog(any()) } returns Result.failure(FailedToLogException(""))
        // When
        //Then
        assertThrows<FailedToLogException> {
            addStateToProjectUseCase(
                projectId = projects[0].id,
                state = "New State"
            )
        }

    }

    @Test

    fun `should add state to project and add log to logs given project id`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(admin)
        every { projectsRepository.getProjectById(any()) } returns Result.success(projects[0])
        // When
        addStateToProjectUseCase(
            projectId = projects[0].id,
            state = "New State"
        )
        //Then
        verify {
            projectsRepository.updateProject(match { it.states.contains("New State") })
        }
        verify { logsRepository.addLog(match { it is AddedLog }) }
    }

    private val admin = User(
        username = "admin",
        hashedPassword = "admin",
        type = UserType.ADMIN
    )
    private val mate = User(
        username = "mate",
        hashedPassword = "mate",
        type = UserType.MATE
    )

    private val projects = listOf(
        Project(
            name = "Project Alpha",
            states = mutableListOf("Backlog", "In Progress", "Done"),
            createdBy = admin.id,
            matesIds = listOf(UUID.fromString("user-234"), UUID.fromString("user-345"), admin.id)
        ),
        Project(
            name = "Project Beta",
            states = mutableListOf("Planned", "Ongoing", "Completed"),
            createdBy = UUID.fromString("user-456"),
            matesIds = listOf(UUID.fromString("user-567"), UUID.fromString("user-678"))
        )
    )
}



