package domain.usecase.task

import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    private val dummyTask = dummyTasks[0]

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
        val task = dummyTask.copy(projectId = dummyProject.id, state = State(name = "test-state"))
        val newState = dummyProject.states.random().name
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns dummyProject

        // When
        editTaskStateUseCase(dummyTask.id, newState)

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
        val task = dummyTask.copy(projectId = dummyProject.id, state = State(name = "test-state"))
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns dummyProject
        every { tasksRepository.updateTask(any()) } throws Exception()
        // when&Then
        assertThrows<Exception> {
            editTaskStateUseCase(task.id, dummyProject.states.random().name)
        }

        verify(exactly = 0) {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }


}

