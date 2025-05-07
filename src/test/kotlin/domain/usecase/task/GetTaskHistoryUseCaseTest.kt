package domain.usecase.task

import com.google.common.truth.Truth.assertThat
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.entity.*
import org.example.domain.repository.LogsRepository
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class GetTaskHistoryUseCaseTest {
    private lateinit var logsRepository: LogsRepository
    private lateinit var getTaskHistoryUseCase: GetTaskHistoryUseCase


    @BeforeEach
    fun setup() {
        logsRepository = mockk()
        getTaskHistoryUseCase = GetTaskHistoryUseCase(logsRepository)
    }

    @Test
    fun `should return list when task in the given list`() {
        // Given
        every { logsRepository.getAllLogs() } returns dummyTasksLogs
        //when
        val result = getTaskHistoryUseCase(dummyTasks[0].id)
        //Then
        assertThat(dummyTasksLogs.subList(1, 3)).containsExactlyElementsIn(result)
    }

    @Test
    fun `should throw Exception when logs fetching fails `() {
        // Given
        every { logsRepository.getAllLogs() } throws Exception()
        // When & Then
        assertThrows<Exception> {
            getTaskHistoryUseCase(dummyTasks[0].id)
        }
    }

    @Test
    fun `should throw NoFoundException list when no logs for the given task `() {
        // Given
        every { logsRepository.getAllLogs() } returns dummyTasksLogs.subList(0, 1)
        //when&//Then
        assertThrows<NotFoundException> {
            getTaskHistoryUseCase(dummyTasks[0].id)
        }
    }


    private val dummyTasksLogs = listOf(
        AddedLog(
            username = "abc",
            affectedId = UUID.randomUUID().toString(),
            affectedType = Log.AffectedType.TASK,
            addedTo = UUID.randomUUID().toString()
        ),
        CreatedLog(
            username = "abc",
            affectedId = dummyTasks[0].id.toString(),
            affectedType = Log.AffectedType.TASK
        ),
        DeletedLog(
            username = "abc",
            affectedId = dummyTasks[0].id.toString(),
            affectedType = Log.AffectedType.TASK,
            deletedFrom = UUID.randomUUID().toString()
        )
    )
}

