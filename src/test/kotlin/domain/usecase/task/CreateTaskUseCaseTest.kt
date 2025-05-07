package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*
import org.example.domain.entity.*
import org.example.domain.repository.UsersRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    private val title = "A Task"
    private val state = "in progress"
    private val projectId  = UUID.randomUUID()
    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(
            tasksRepository = tasksRepository,
            logsRepository = logsRepository,
            usersRepository = usersRepository
        )
    }

    @Test
    fun `should not complete creation of task when get current user is null`() {
        // Given
        every { usersRepository.getCurrentUser() } returns null

        // When
        createTaskUseCase.invoke(title = title , state = state , projectId = projectId)

        // then
        verify (exactly = 0){ tasksRepository.addTask(any()) }
    }


    @Test
    fun `should update task`() {
        // when
        createTaskUseCase.invoke(title = title , state = state , projectId = projectId)
        // then
        verify { tasksRepository.addTask(any()) }
    }

    @Test
    fun `should add log for addition of  task`() {
        // when
        createTaskUseCase.invoke(title = title , state = state , projectId = projectId)
        // then
        verify { logsRepository.addLog(any()) }
    }

}
