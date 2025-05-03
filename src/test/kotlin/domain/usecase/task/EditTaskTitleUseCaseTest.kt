package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditTaskTitleUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    lateinit var editTaskTitleUseCase: EditTaskTitleUseCase

    @BeforeEach
    fun setUp() {
        editTaskTitleUseCase = EditTaskTitleUseCase(authenticationRepository, tasksRepository, logsRepository)
    }

    @Test
    fun `invoke should throw NoTaskFoundException when there is no current user return failure`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())
        // when & then
        assertThrows<UnauthorizedException> {
            editTaskTitleUseCase.invoke(taskId = "15", title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should throw NoFoundException when tasks is empty in tasksRepository`() {
        // given
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(
                username = "ahmed",
                hashedPassword = "902865934",
                type = UserType.MATE,
            )
        )
        every { tasksRepository.getAll() } returns Result.failure(NoFoundException())
        // when & then
        assertThrows<NoFoundException> {
            editTaskTitleUseCase.invoke(taskId = "15", title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should throw NoFoundException when add log get failure`() {
        // given
        val tasks = listOf(
            Task(
                id = "24",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            ),
            Task(
                id = "12",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            )
        )
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(
                username = "ahmed",
                hashedPassword = "902865934",
                type = UserType.MATE,
            )
        )
        every { tasksRepository.getAll() } returns Result.success(tasks)
        every { logsRepository.add(any()) } returns Result.failure(NoFoundException())
        // when & then
        assertThrows<NoFoundException> {
            editTaskTitleUseCase.invoke(taskId = "12", title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should throw NoFoundException when update task get failure `() {
        // given
        val tasks = listOf(
            Task(
                id = "24",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            ),
            Task(
                id = "12",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            )
        )
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(
                username = "ahmed",
                hashedPassword = "902865934",
                type = UserType.MATE,
            )
        )
        every { tasksRepository.getAll() } returns Result.success(tasks)
        every { logsRepository.add(any()) } returns Result.success(Unit)
        every { tasksRepository.update(any())} returns Result.failure(NoFoundException())
        // when & then
        assertThrows<NoFoundException> {
            editTaskTitleUseCase.invoke(taskId = "12", title = "get the projects from repo")
        }
    }


    @Test
    fun `invoke should throw NoFoundException when task not found in task list of getAll of tasksRepository`() {
        // given
        val tasks = listOf(
            Task(
                id = "24",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            ),
            Task(
                id = "12",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            )
        )
        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "2342143",
                type = UserType.MATE,
            )
        )
        every { tasksRepository.getAll() } returns Result.success(tasks)
        // when & then
        assertThrows<NoFoundException> {
            editTaskTitleUseCase.invoke(taskId = "15", title = "get the projects from repo")
        }
    }

    @Test
    fun `invoke should complete edit Task when the task is found`() {
        //grean
        // given
        val tasks = listOf(
            Task(
                id = "24",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            ),
            Task(
                id = "12",
                title = "Auth Feature",
                state = "in progress",
                assignedTo = listOf("ahmed", "medo"),
                createdBy = "Ahmed",
                projectId = "234"
            )
        )

        every { authenticationRepository.getCurrentUser() } returns Result.success(
            User(
                username = "Ahmed",
                hashedPassword = "2342143",
                type = UserType.MATE,
            )
        )
        every { tasksRepository.getAll() } returns Result.success(tasks)

        editTaskTitleUseCase.invoke(taskId = "12", title = "get the projects from repo")


        verify { logsRepository.add(any()) }
        verify { tasksRepository.update(any()) }

    }

}