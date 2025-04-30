package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import org.example.domain.*
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.GetTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class GetTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var getTaskUseCase: GetTaskUseCase

    private val taskId = "T1"
    private val username = "admin1"

    private val adminUser = User(
        id = "U1",
        username = username,
        password = "pass1",
        type = UserType.ADMIN
    )

    private val mateUser = User(
        id = "U1",
        username = username,
        password = "pass1",
        type = UserType.MATE
    )

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        authenticationRepository = mockk(relaxed = true)
        getTaskUseCase = GetTaskUseCase(tasksRepository, authenticationRepository)
    }

    @Test
    fun `should return task when user is authorized and task exists`() {
        // Given
        val task = Task(
            id = taskId,
            title = "Task 1",
            state = "ToDo",
            assignedTo = emptyList(),
            createdBy = username,
            cratedAt = LocalDateTime.now(),
            projectId = "P1"
        )
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)

        // When
        val result = getTaskUseCase(taskId)

        // Then
        assert(result == task)
    }

    @Test
    fun `should throw UnauthorizedException when getCurrentUser fails`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        // When & Then
        assertThrows<UnauthorizedException> {
            getTaskUseCase(taskId)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is not authorized`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(mateUser)

        // When & Then
        assertThrows<AccessDeniedException> {
            getTaskUseCase(taskId)
        }
    }


    @Test
    fun `should throw InvalidTaskIdException when taskId is blank`() {
        // Given
        val blankTaskId = ""

        // When & Then
        assertThrows<InvalidIdException> {
            getTaskUseCase(blankTaskId)
        }
    }

    @Test
    fun `should throw TaskNotFoundException when task does not exist`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(taskId) } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            getTaskUseCase(taskId)
        }
    }
}