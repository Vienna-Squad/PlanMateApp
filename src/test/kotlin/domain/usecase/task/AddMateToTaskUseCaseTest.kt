package domain.usecase.task

import dummyMate
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddMateToTaskUseCaseTest {

    private lateinit var addMateToTaskUseCase: AddMateToTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    val taskId = UUID.randomUUID()  // Random UUID
    val mateId = UUID.randomUUID()  // Random UUID
    val projectId = UUID.randomUUID()  // Random UUID

    @BeforeEach
    fun setup() {
        addMateToTaskUseCase = AddMateToTaskUseCase(
            tasksRepository,
            logsRepository,
            usersRepository,
            projectsRepository,
        )
    }

    @Test
    fun `should call get task by id`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project

        // when
        addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id)
        // then
        verify { tasksRepository.getTaskById(any()) }
    }

    @Test
    fun `should update task`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        // when
        addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id)
        // then
        verify { tasksRepository.updateTask(any()) }
    }

    @Test
    fun `should add log for addition of mate to task`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        // when
        addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id)
        // then
        verify { logsRepository.addLog(any()) }
    }

}
