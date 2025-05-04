package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import org.example.domain.*
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.GetTaskUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*


class GetTaskUseCaseTest {

        // Mock repositories
        private lateinit var tasksRepository: TasksRepository
        private lateinit var authRepository: AuthRepository
        private lateinit var getTaskUseCase: GetTaskUseCase

        @BeforeEach
        fun setup() {
            tasksRepository = mockk(relaxed = true)
            authRepository = mockk(relaxed = true)
            getTaskUseCase = GetTaskUseCase(tasksRepository, authRepository)
        }

        @Test
        fun `getTask should return task when user is admin regardless of assignment`() {
            // Given: Admin user and any task (even unassigned)
            every { authRepository.getCurrentUser() } returns Result.success(adminUser)
            every { tasksRepository.getTaskById(taskId) } returns Result.success(baseTask)

            // When
            val result = getTaskUseCase(taskId)

            // Then: Admin can access any task
            assertEquals(baseTask, result)
        }

        @Test
        fun `getTask should return task when mate user is assigned to the task`() {
            // Given: Task is assigned to mate user
            val assignedTask = baseTask.copy(assignedTo = listOf(mateUserId))
            every { authRepository.getCurrentUser() } returns Result.success(mateUser)
            every { tasksRepository.getTaskById(taskId) } returns Result.success(assignedTask)

            // When
            val result = getTaskUseCase(taskId)

            // Then: Mate can access assigned tasks
            assertEquals(assignedTask, result)
        }

        @Test
        fun `getTask should return task when user is the creator of the task`() {
            // Given: Task was created by mate user
            val creatorTask = baseTask.copy(createdBy = mateUserId)
            every { authRepository.getCurrentUser() } returns Result.success(mateUser)
            every { tasksRepository.getTaskById(taskId) } returns Result.success(creatorTask)

            // When
            val result = getTaskUseCase(taskId)

            // Then: Creator can access their own tasks
            assertEquals(creatorTask, result)
        }

        @Test
        fun `getTask should throw UnauthorizedException when mate user is not assigned or creator`() {
            // Given: Task belongs to someone else and isn't assigned to current user
            val otherUserTask = baseTask.copy(
                createdBy = otherUserId,
                assignedTo = listOf(otherUserId)
            )
            every { authRepository.getCurrentUser() } returns Result.success(strangerUser)
            every { tasksRepository.getTaskById(taskId) } returns Result.success(otherUserTask)

            // When & Then: Regular user can't access unrelated tasks
            assertThrows<UnauthorizedException> {
                getTaskUseCase(taskId)
            }
        }

        @Test
        fun `getTask should throw UnauthorizedException when authentication fails`() {
            // Given: Authentication fails
            every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

            // When & Then: Should propagate authentication failure
            assertThrows<UnauthorizedException> {
                getTaskUseCase(taskId)
            }
        }

        @Test
        fun `getTask should throw NotFoundException when task doesn't exist`() {
            // Given: Task doesn't exist (but user is valid)
            every { authRepository.getCurrentUser() } returns Result.success(adminUser)
            every { tasksRepository.getTaskById(taskId) } returns Result.failure(NotFoundException(""))

            // When & Then: Should propagate not found error
            assertThrows<NotFoundException> {
                getTaskUseCase(taskId)
            }
        }

    // Test UUIDs
    private val taskId = UUID.randomUUID()
    private val adminUserId = UUID.randomUUID()
    private val mateUserId = UUID.randomUUID()
    private val strangerUserId = UUID.randomUUID()
    private val otherUserId = UUID.randomUUID()
    private val projectId = UUID.randomUUID()

    // Test users
    private val adminUser = User(
        id = adminUserId,
        username = "admin1",
        hashedPassword = "hashedPass1",
        role = UserRole.ADMIN,

        )

    private val mateUser = User(
        id = mateUserId,
        username = "mate",
        hashedPassword = "hashedPass2",
        role = UserRole.MATE,
    )

    private val strangerUser = User(
        id = strangerUserId,
        username = "stranger",
        hashedPassword = "hashedPass3",
        role = UserRole.MATE,
    )

    // Base task
    private val baseTask = Task(
        id = taskId,
        title = "Task 1",
        state = "ToDo",
        assignedTo = emptyList(),
        createdBy = adminUserId,
        createdAt = LocalDateTime.now(),
        projectId = projectId
    )

}