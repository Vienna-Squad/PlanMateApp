package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyProject
import dummyProjectId
import dummyState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.ProjectAccessDeniedException
import org.example.domain.StateAlreadyExistsException
import org.example.domain.entity.State
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AddStateToProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    private val projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val state = "done.."

    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        addStateToProjectUseCase =
            AddStateToProjectUseCase(projectsRepository, logsRepository, usersRepository)

    }


    @Test
    fun `should throw AccessDeniedException when who creates project is not current user`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject
        // when & then
        assertThrows<ProjectAccessDeniedException> {
            addStateToProjectUseCase.invoke(projectId = dummyProjectId, stateName = dummyState)
        }
    }

    @Test
    fun `should throw AlreadyExistException when mate already found in project`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(
            id = dummyProjectId,
            createdBy = dummyAdmin.id,
            states = listOf(State(name = dummyState)),
        )
        // when & then
        assertThrows<StateAlreadyExistsException> {
            addStateToProjectUseCase.invoke(projectId = dummyProjectId, stateName = dummyState)
        }
    }

    @Test
    fun `should complete addition of state in project`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(
            id = dummyProjectId,
            createdBy = dummyAdmin.id,
            states = listOf(),
        )
        // when
        addStateToProjectUseCase(projectId = projectId, state)
        // then
        verify { projectsRepository.updateProject(any()) }
        verify { logsRepository.addLog(any()) }
    }

}



