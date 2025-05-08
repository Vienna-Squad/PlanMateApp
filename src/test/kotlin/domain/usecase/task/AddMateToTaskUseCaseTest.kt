package domain.usecase.task

import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.*
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class AddMateToTaskUseCaseTest {

    private lateinit var addMateToTaskUseCase: AddMateToTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    val taskId = UUID.randomUUID()  // Random UUID
    val mateId = UUID.randomUUID()  // Random UUID
    val projectId = UUID.randomUUID()  // Random UUID

    @BeforeEach
    fun setup() {
        addMateToTaskUseCase = AddMateToTaskUseCase(
            tasksRepository,
            logsRepository,
            mockk(relaxed = true)
        )
    }
    @Test
    fun `should call get task by id`() {
        // when
        addMateToTaskUseCase.invoke(taskId = taskId , mateId = mateId)
        // then
        verify { tasksRepository.getTaskById(any()) }
    }

    @Test
    fun `should update task`() {
        // when
        addMateToTaskUseCase.invoke(taskId = taskId , mateId = mateId)
        // then
        verify { tasksRepository.updateTask(any()) }
    }

    @Test
    fun `should add log for addition of mate to task`() {
        // when
        addMateToTaskUseCase.invoke(taskId = taskId , mateId = mateId)
        // then
        verify { logsRepository.addLog(any()) }
    }

}
