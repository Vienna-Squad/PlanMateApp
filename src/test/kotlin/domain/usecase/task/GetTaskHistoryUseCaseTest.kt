package domain.usecase.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class GetTaskHistoryUseCaseTest {
    private lateinit var logsRepository: LogsRepository
    private lateinit var authenticationRepository: AuthenticationRepository

    private lateinit var getTaskHistoryUseCase: GetTaskHistoryUseCase


    @BeforeEach
    fun setup() {
        logsRepository = mockk()
        authenticationRepository = mockk(relaxed = true)
        getTaskHistoryUseCase = GetTaskHistoryUseCase(authenticationRepository, logsRepository)
    }

    @Test
    fun `should throw UnauthorizedException given no logged-in user is found`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(Exception())
        // When & Then
        assertThrows<UnauthorizedException> {
            getTaskHistoryUseCase(dummyTask.id)
        }
    }

    @Test
    fun `should throw NoTaskFoundException when logsRepository throws an exception`() {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = " A Task",
            state = "in progress",
            assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()), // Random assigned users
            createdBy = UUID.randomUUID(),
            projectId = UUID.randomUUID()
        )
        every { logsRepository.getAllLogs() } returns Result.failure(Exception())
        // When & Then
        assertThrows<NotFoundException> { getTaskHistoryUseCase(task.id) }
    }

    @Test
    fun `should throw NoTaskFoundException when task is not found in the given list`() {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = " A Task",
            state = "in progress",
            assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()), // Random assigned users
            createdBy = UUID.randomUUID(),
            projectId = UUID.randomUUID()
        )
        every { logsRepository.getAllLogs() } returns Result.success(dummyLogs)
        // When & Then
        assertThrows<NotFoundException> { getTaskHistoryUseCase(task.id) }
    }

    @Test
    fun `should return list of logs associated with a specific task given task id`() {
        // Given
        every { logsRepository.getAllLogs() } returns Result.success(dummyLogs)
        // When
        val result = getTaskHistoryUseCase(dummyTask.id)
        // Then
        assertThat(dummyLogs).containsExactlyElementsIn(result)
    }

    private val dummyTask = Task(
        id = UUID.randomUUID(),
        title = " A Task",
        state = "in progress",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    )

    private val dummyLogs = listOf(
        AddedLog(
            username = "abc",
            affectedId = dummyTask.id,
            affectedType = Log.AffectedType.TASK,
            addedTo = UUID.randomUUID()
        ),
        CreatedLog(
            username = "abc",
            affectedId = dummyTask.id,
            affectedType = Log.AffectedType.TASK
        ),
        DeletedLog(
            username = "abc",
            affectedId = dummyTask.id,
            affectedType = Log.AffectedType.TASK,
            deletedFrom = UUID.randomUUID().toString() // Random project ID
        )
    )
}

