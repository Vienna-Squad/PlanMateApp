package domain.usecase.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.AddedLog
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class AddMateToTaskUseCaseTest {

    private lateinit var addMateToTaskUseCase: AddMateToTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        addMateToTaskUseCase = AddMateToTaskUseCase(tasksRepository, logsRepository, authenticationRepository)
    }

    @Test
    fun `should add mate to task and log the action successfully`() {
        // Given
        val taskId = "task-123"
        val mateId = "user-456"
        val currentUser = createTestUser(id = "user-123", username = "creator")
        val task = createTestTask(id = taskId, assignedTo = emptyList())
        val mate = createTestUser(id = mateId)
        val updatedTask = task.copy(assignedTo = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(mateId) } returns Result.success(mate)

        // When
        addMateToTaskUseCase(taskId, mateId)

        // Then
        verify { tasksRepository.update(updatedTask) }
        verify { logsRepository.add(any<AddedLog>()) }
        assertThat(updatedTask.assignedTo).containsExactly(mateId)
    }

    @Test
    fun `should throw InvalidIdException when task does not exist`() {
        // Given
        val taskId = "non-existent-task"
        val mateId = "user-456"
        val currentUser = createTestUser(id = "user-123")

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.failure(InvalidIdException())

        // When & Then
        assertThrows<InvalidIdException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw NoFoundException when mate does not exist`() {
        // Given
        val taskId = "task-123"
        val mateId = "non-existent-user"
        val currentUser = createTestUser(id = "user-123")
        val task = createTestTask(id = taskId)

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(mateId) } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should add mate to task with existing mates`() {
        // Given
        val taskId = "task-123"
        val existingMateId = "user-789"
        val newMateId = "user-456"
        val currentUser = createTestUser(id = "user-123")
        val task = createTestTask(id = taskId, assignedTo = listOf(existingMateId))
        val mate = createTestUser(id = newMateId)
        val updatedTask = task.copy(assignedTo = listOf(existingMateId, newMateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(newMateId) } returns Result.success(mate)

        // When
        addMateToTaskUseCase(taskId, newMateId)

        // Then
        verify { tasksRepository.update(updatedTask) }
        verify { logsRepository.add(any<AddedLog>()) }
        assertThat(updatedTask.assignedTo).containsExactly(existingMateId, newMateId)
    }

    @Test
    fun `should not update task if mate is already assigned`() {
        // Given
        val taskId = "task-123"
        val mateId = "user-456"
        val currentUser = createTestUser(id = "user-123")
        val task = createTestTask(id = taskId, assignedTo = listOf(mateId))
        val mate = createTestUser(id = mateId)

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(mateId) } returns Result.success(mate)

        // When
        addMateToTaskUseCase(taskId, mateId)

        // Then
        verify { tasksRepository.update(task) }
        verify { logsRepository.add(any<AddedLog>()) }
        assertThat(task.assignedTo).containsExactly(mateId)
    }

    @Test
    fun `should throw NoFoundException when task update fails`() {
        // Given
        val taskId = "task-123"
        val mateId = "user-456"
        val currentUser = createTestUser(id = "user-123")
        val task = createTestTask(id = taskId, assignedTo = emptyList())
        val mate = createTestUser(id = mateId)
        val updatedTask = task.copy(assignedTo = listOf(mateId))

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(mateId) } returns Result.success(mate)
        every { tasksRepository.update(updatedTask) } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw NoFoundException when log addition fails`() {
        // Given
        val taskId = "task-123"
        val mateId = "user-456"
        val currentUser = createTestUser(id = "user-123")
        val task = createTestTask(id = taskId, assignedTo = emptyList())
        val mate = createTestUser(id = mateId)

        every { authenticationRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.get(taskId) } returns Result.success(task)
        every { authenticationRepository.getUser(mateId) } returns Result.success(mate)
        every { logsRepository.add(any<AddedLog>()) } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user not found`() {
        // Given
        val taskId = "task-123"
        val mateId = "user-456"

        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        // When & Then
        assertThrows<UnauthorizedException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw InvalidIdException when taskId is empty`() {
        // Given
        val taskId = ""
        val mateId = "user-456"

        // When & Then
        assertThrows<InvalidIdException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }


    @Test
    fun `should throw InvalidIdException when mateId is empty`() {
        // Given
        val taskId = "task-123"
        val mateId = ""

        // When & Then
        assertThrows<InvalidIdException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }


    private fun createTestTask(
        id: String = UUID.randomUUID().toString(),
        title: String = "Test Task",
        state: String = "todo",
        assignedTo: List<String> = emptyList(),
        createdBy: String = "test-user",
        projectId: String = "project-123"
    ): Task {
        return Task(
            id = id,
            title = title,
            state = state,
            assignedTo = assignedTo,
            createdBy = createdBy,
            projectId = projectId,
            cratedAt = LocalDateTime.now()
        )
    }

    private fun createTestUser(
        id: String = UUID.randomUUID().toString(),
        username: String = "testUser",
        password: String = "hashed",
        type: UserType = UserType.MATE
    ): User {
        return User(
            id = id,
            username = username,
            password = password,
            type = type,
            cratedAt = LocalDateTime.now()
        )
    }
}