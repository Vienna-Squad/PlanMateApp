package domain.usecase.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NoFoundException
import org.example.domain.entity.*
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetTaskHistoryUseCaseTest {
    private lateinit var logsRepository: LogsRepository
    private lateinit var getTaskHistoryUseCase: GetTaskHistoryUseCase


    @BeforeEach
    fun setup() {
        logsRepository = mockk()
        getTaskHistoryUseCase = GetTaskHistoryUseCase(logsRepository)
    }


    @Test
    fun `should return list of logs associated with a specific task given task id`() {
        //Given

        every { logsRepository.getAll() } returns Result.success(dummyLogs)
        //when
        val result = getTaskHistoryUseCase.invoke(dummyTask.id)
        //then
        assertThat(dummyLogs).containsExactlyElementsIn(result)
    }

    @Test
    fun `should throw NoTaskFoundException when task is not found in the given list`() {
        //Given
        val task = Task(
            title = " A Task",
            state = "in progress",
            assignedTo = listOf("12", "123"),
            createdBy = "1",
            projectId = "999"
        )
        every { logsRepository.getAll() } returns Result.success(dummyLogs)

        //when&then
        assertThrows<NoFoundException> { getTaskHistoryUseCase.invoke(task.id) }
    }


    val dummyTask = Task(
        title = " A Task",
        state = "in progress",
        assignedTo = listOf("12", "123"),
        createdBy = "1",
        projectId = "999"
    )
    val dummyLogs = listOf(
        AddedLog(
            username = "abc",
            affectedId = dummyTask.id,
            affectedType = Log.AffectedType.TASK,
            addedTo = "999"
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
            deletedFrom = "999"
        )

    )


}

