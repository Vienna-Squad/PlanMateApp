package domain.usecase.task

import dummyAdmin
import dummyMate
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.CreatedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateTaskUseCaseTest {
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {

        createTaskUseCase = CreateTaskUseCase(
            tasksRepository = tasksRepository,
            logsRepository = logsRepository,
            usersRepository = usersRepository,
            projectsRepository = projectsRepository,
        )
    }


    @Test
    fun `should create task when project creator create one`() {
        //given
        val title = "new title"
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val projectState = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        // when
        createTaskUseCase(title = title, stateName = projectState.name, projectId = project.id)
        // then
        verify { tasksRepository.addTask(match { it.title == title && it.state.name == projectState.name }) }
        verify { logsRepository.addLog(match { it is CreatedLog }) }
    }

    @Test
    fun `should create task when project mate create one`() {
        //given
        val title = "new title"
        val project = dummyProject.copy(matesIds = listOf(dummyMate.id))
        val projectState = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(project.id) } returns project
        // when
        createTaskUseCase(title = title, stateName = projectState.name, projectId = project.id)
        // then
        verify { tasksRepository.addTask(match { it.title == title && it.state.name == projectState.name }) }
        verify { logsRepository.addLog(match { it is CreatedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when non-project-related user create one`() {
        //given
        val title = "new title"
        val projectState = dummyProject.states.random()
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        // when && then
        assertThrows<AccessDeniedException> {
            createTaskUseCase(
                title = title,
                stateName = projectState.name,
                projectId = dummyProject.id
            )
        }
        verify(exactly = 0) { tasksRepository.addTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when user create one with non-project-related state`() {
        //given
        val title = "new title"
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        // when && when
        assertThrows<ProjectHasNoException> {createTaskUseCase(title = title, stateName = "non-project-related", projectId = project.id)}
        verify(exactly = 0) { tasksRepository.addTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

}