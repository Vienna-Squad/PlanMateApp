package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NoProjectFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddStateToProjectUseCaseTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk()
        projectsRepository = mockk()
        addStateToProjectUseCase = AddStateToProjectUseCase(authenticationRepository, projectsRepository)

    }

    @Test
    fun `should throw NoProjectFoundException when attempting to add a state to a non-existent project`() {
        //Given
        every { projectsRepository.getAll().getOrNull() } returns projects
        // When & Then
        assertThrows<NoProjectFoundException> {
            addStateToProjectUseCase.invoke(
                projectId = "non-existent project",
                state = "New State"
            )
        }

    }

    @Test

    fun `should throw UnauthorizedException when attempting to add a state to project given current user is not admin`() {
        //Given
        every { authenticationRepository.getCurrentUser().getOrNull() } returns mate
        // Then&&When
        assertThrows<UnauthorizedException> {
            addStateToProjectUseCase.invoke(
                projectId = "non-existent project",
                state = "New State"
            )
        }

    }

    @Test

    fun `should add state to project given project id`() {
        // Given
        every { authenticationRepository.getCurrentUser().getOrNull() } returns admin
        // When
        every { projectsRepository.getAll().getOrNull() } returns projects
        // Then
        addStateToProjectUseCase.invoke(
            projectId = projects[0].id,
            state = "New State"
        )
        // Then
        verify {
            projectsRepository.add(
                match { it.id == projects[0].id && it.states.contains("New State") }
            )
        }

    }


    val projects = listOf(
        Project(
            name = "Project Alpha",
            states = mutableListOf("Backlog", "In Progress", "Done"),
            createdBy = "user-123",
            matesIds = listOf("user-234", "user-345")
        ),
        Project(
            name = "Project Beta",
            states = mutableListOf("Planned", "Ongoing", "Completed"),
            createdBy = "user-456",
            matesIds = listOf("user-567", "user-678")
        )
    )
    val admin = User(
        username = "admin",
        password = "admin",
        type = UserType.ADMIN
    )
    val mate = User(
        username = "mate",
        password = "mate",
        type = UserType.MATE
    )


}