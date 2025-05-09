package domain.usecase.task

import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CreateTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var projectsRepository: ProjectsRepository

    private val title = "A Task"
    private val state = "in progress"
    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        projectsRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(
            tasksRepository = tasksRepository,
            logsRepository = logsRepository,
            usersRepository = usersRepository,
            projectsRepository = projectsRepository,
        )
    }


    @Test
    fun `should update task`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        // when
        createTaskUseCase(title = title, stateName = dummyProject.states.random().name, projectId = dummyProject.id)
        // then
        verify { tasksRepository.addTask(any()) }
    }

    @Test
    fun `should add log for addition of  task`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        // when
        createTaskUseCase(title = title, stateName = dummyProject.states.random().name, projectId = dummyProject.id)
        // then
        verify { logsRepository.addLog(any()) }
    }

}
