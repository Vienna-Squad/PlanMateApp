package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        authenticationRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(tasksRepository, logsRepository, authenticationRepository)
    }
    @Test
    fun `should throw UnauthorizedException when no logged-in user is found`() {
        //Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(Exception())
        // Then&&When
        assertThrows<UnauthorizedException> {
            createTaskUseCase(createTask())
        }
    }
    @Test
    fun `should add task and add log it in logs repository`() {
        //given
        val task = createTask()
        val user = createUser()
        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        //when
        createTaskUseCase.invoke(task)
        //then
        verify { tasksRepository.add(any()) }
        verify { logsRepository.add(any()) }
    }
}

fun createTask(): Task {
    return Task(
        title = " A Task",
        state = "in progress",
        assignedTo = listOf("12", "123"),
        createdBy = "12",
        projectId = "999"
    )
}

fun createUser(): User {
    return User(
        username = "firstuser",
        password = "1234",
        type = UserType.MATE
    )
}
