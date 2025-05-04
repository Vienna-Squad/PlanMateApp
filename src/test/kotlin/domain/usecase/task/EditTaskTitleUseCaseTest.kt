package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditTaskTitleUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    lateinit var editTaskTitleUseCase: EditTaskTitleUseCase

    @BeforeEach
    fun setUp() {
        editTaskTitleUseCase = EditTaskTitleUseCase(authRepository, tasksRepository, logsRepository)
    }

    @Test
    fun `invoke should throw NoTaskFoundException when there is no current user return failure`() {
        // Given
        val randomTaskId = UUID.randomUUID()
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

        // When & Then
        assertThrows<UnauthorizedException> {
            editTaskTitleUseCase.invoke(taskId = randomTaskId, title = "get the projects from repo")
        }
    }
    @Test
    fun `invoke should throw NoFoundException when tasks is empty in tasksRepository`() {
        // Given
        val randomTaskId = UUID.randomUUID()
        val randomUserId = UUID.randomUUID()

        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                id = randomUserId,
                username = "ahmed",
                hashedPassword = "902865934",
                role = UserRole.MATE,
            )
        )
        every { tasksRepository.getAllTasks() } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            editTaskTitleUseCase.invoke(taskId = randomTaskId, title = "get the projects from repo")
        }
    }
    @Test
    fun `invoke should throw NoFoundException when add log get failure`() {
        // Given
        val randomTaskId1 = UUID.randomUUID()
        val randomTaskId2 = UUID.randomUUID()
        val randomUserId = UUID.randomUUID()

        val tasks = listOf(
            Task(
                id = randomTaskId1,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(randomUserId, UUID.randomUUID()),
                createdBy = randomUserId,
                projectId = UUID.randomUUID()
            ),
            Task(
                id = randomTaskId2,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(randomUserId, UUID.randomUUID()),
                createdBy = randomUserId,
                projectId = UUID.randomUUID()
            )
        )

        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                id = randomUserId,
                username = "ahmed",
                hashedPassword = "902865934",
                role = UserRole.MATE,
            )
        )
        every { tasksRepository.getAllTasks() } returns Result.success(tasks)
        every { logsRepository.addLog(any()) } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            editTaskTitleUseCase.invoke(taskId = randomTaskId2, title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should throw NoFoundException when update task get failure`() {
        // given
        val randomTaskId1 = UUID.randomUUID()
        val randomTaskId2 = UUID.randomUUID()
        val randomUserId = UUID.randomUUID()

        val tasks = listOf(
            Task(
                id = randomTaskId1,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(randomUserId, UUID.randomUUID()),
                createdBy = randomUserId,
                projectId = UUID.randomUUID()
            ),
            Task(
                id = randomTaskId2,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(randomUserId, UUID.randomUUID()),
                createdBy = randomUserId,
                projectId = UUID.randomUUID()
            )
        )

        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                id = randomUserId,
                username = "ahmed",
                hashedPassword = "902865934",
                role = UserRole.MATE,
            )
        )
        every { tasksRepository.getAllTasks() } returns Result.success(tasks)
        every { logsRepository.addLog(any()) } returns Result.success(Unit)
        every { tasksRepository.updateTask(any())} returns Result.failure(NotFoundException(""))

        // when & then
        assertThrows<NotFoundException> {
            editTaskTitleUseCase.invoke(taskId = randomTaskId2, title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should throw NoFoundException when task not found in task list of getAll of tasksRepository`() {
        // given
        val randomTaskId1 = UUID.randomUUID()
        val randomTaskId2 = UUID.randomUUID()

        val tasks = listOf(
            Task(
                id = randomTaskId1,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
                createdBy = UUID.randomUUID(),
                projectId = UUID.randomUUID()
            ),
            Task(
                id = randomTaskId2,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
                createdBy = UUID.randomUUID(),
                projectId = UUID.randomUUID()
            )
        )

        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                id = UUID.randomUUID(),
                username = "Ahmed",
                hashedPassword = "2342143",
                role = UserRole.MATE,
            )
        )
        every { tasksRepository.getAllTasks() } returns Result.success(tasks)

        // when & then
        assertThrows<NotFoundException> {
            editTaskTitleUseCase.invoke(taskId = UUID.randomUUID(), title = "get the projects from repo")
        }
    }
    @Test
    fun `invoke should complete edit Task when the task is found`() {
        // given
        val randomTaskId1 = UUID.randomUUID()
        val randomTaskId2 = UUID.randomUUID()

        val tasks = listOf(
            Task(
                id = randomTaskId1,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
                createdBy = UUID.randomUUID(),
                projectId = UUID.randomUUID()
            ),
            Task(
                id = randomTaskId2,
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()), // Random assigned users
                createdBy = UUID.randomUUID(),
                projectId = UUID.randomUUID()
            )
        )

        every { authRepository.getCurrentUser() } returns Result.success(
            User(
                id = UUID.randomUUID(),
                username = "Ahmed",
                hashedPassword = "2342143",
                role = UserRole.MATE,
            )
        )
        every { tasksRepository.getAllTasks() } returns Result.success(tasks)

        // when
        editTaskTitleUseCase.invoke(taskId = randomTaskId2, title = "get the projects from repo")

        // then
        verify { logsRepository.addLog(any()) }
        verify { tasksRepository.updateTask(any()) }
    }


}