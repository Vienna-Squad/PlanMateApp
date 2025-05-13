package domain.usecase.task

import dummyTasks
import io.mockk.every
import io.mockk.mockk
import org.example.domain.LogsNotFound
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertTrue


class GetTaskHistoryUseCaseTest {
    private val logsRepository: LogsRepository = mockk()
    private lateinit var getTaskHistoryUseCase: GetTaskHistoryUseCase
    private val task = dummyTasks[0]

    @BeforeEach
    fun setup() {
        getTaskHistoryUseCase = GetTaskHistoryUseCase(logsRepository)
    }

    @Test
    fun `should return list of logs when task logs exist`() {
        // Given
        every { logsRepository.getAllLogs() } returns dummyTasksLogs
        //when
        val result = getTaskHistoryUseCase(task.id)
        //Then
        assertTrue { result.all { it.toString().contains(task.id.toString()) } }
    }

    @Test
    fun `should throw Exception when logs fetching fails `() {
        // Given
        every { logsRepository.getAllLogs() } throws Exception()
        // When & Then
        assertThrows<Exception> {
            getTaskHistoryUseCase(task.id)
        }
    }

    @Test
    fun `should throw NoFoundException list when no logs for the given task id`() {
        // Given
        val dummyLogs = dummyTasksLogs.subList(0, 1)
        every { logsRepository.getAllLogs() } returns dummyLogs
        //when&//Then
        assertThrows<LogsNotFound> {
            getTaskHistoryUseCase(task.id)
        }
    }

    private val dummyTasksLogs = listOf(
        AddedLog(
            username = "abc",
            affectedId = UUID.randomUUID(),
            affectedName = "T-101",
            affectedType = Log.AffectedType.TASK,
            addedTo = UUID.randomUUID().toString()
        ),
        CreatedLog(
            username = "abc",
            affectedId = dummyTasks[0].id,
            affectedName = "T-101",
            affectedType = Log.AffectedType.TASK
        ),
        DeletedLog(
            username = "abc",
            affectedId = dummyTasks[0].id,
            affectedName = "T-101",
            affectedType = Log.AffectedType.TASK,
            deletedFrom = UUID.randomUUID().toString()
        )
    )
}

