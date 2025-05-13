package domain.usecase.task

import dummyAdmin
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.TaskAccessDeniedException
import org.example.domain.entity.State
import org.example.domain.entity.log.ChangedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditTaskStateUseCaseTest {
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)


    @BeforeEach
    fun setup() {
        editTaskStateUseCase = EditTaskStateUseCase(
            tasksRepository,
            logsRepository,
            usersRepository,
            projectsRepository
        )
    }

    @Test
    fun `should edit task state when task exists`() {
        // Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(projectId = project.id, state = State(name = "test-state"))
        val newState = project.states.random().name

        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        // When
        editTaskStateUseCase(task.id, newState)
        // Then
        verify {
            tasksRepository.updateTask(match {
                it.state.name == newState && it.id == dummyTask.id
            })
        }
        verify {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }

    @Test
    fun `should throw TaskAccessDeniedException when project is not created by current user given task id & new state`() {
        // Given
        val project = dummyProject
        val task = dummyTask.copy(projectId = project.id, state = State(name = "test-state"))
        val newState = project.states.random().name

        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        // When
        assertThrows<TaskAccessDeniedException> {
            editTaskStateUseCase(task.id, newState)
        }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyProject.id, "new name")
        }
        verify(exactly = 0) { tasksRepository.getTaskById(any()) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getTaskById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyProject.id, "new name")
        }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns dummyTask
        every { projectsRepository.getProjectById(dummyTask.projectId) } throws Exception()
        //when && then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyTask.id, "new name")
        }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw an Exception and not log when getTaskById fails `() {
        // Given

        every { tasksRepository.getTaskById(dummyTask.id) } throws Exception()

        // when&Then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyTask.id, "In Progress")
        }
        verify(exactly = 0) {
            tasksRepository.updateTask(match {
                it.id == dummyTask.id
            })
        }
        verify(exactly = 0) {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }

    @Test
    fun `should throw an Exception and not log when updateTask fails `() {
        // Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(projectId = project.id, state = State(name = "test-state"))
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { tasksRepository.updateTask(any()) } throws Exception()
        // when&Then
        assertThrows<Exception> {
            editTaskStateUseCase(task.id, project.states.random().name)
        }

        verify(exactly = 0) {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }

    private val dummyTask = dummyTasks[0]
}

