package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.Task
import org.example.domain.repository.UsersRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID


class EditTaskTitleUseCaseTest {

    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    lateinit var editTaskTitleUseCase: EditTaskTitleUseCase

    @BeforeEach
    fun setUp() {
        editTaskTitleUseCase = EditTaskTitleUseCase( tasksRepository, logsRepository,mockk(relaxed = true))
    }

    @Test
    fun `invoke should edit task when the task id is valid`() {
        // given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Auth Feature",
            state = "in progress",
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            projectId = UUID.randomUUID()
        )

        every { tasksRepository.updateTask(any()) } returns Unit

        editTaskTitleUseCase.invoke(taskId = task.id , newTitle = "School Library" )

    }

    @Test
    fun `invoke should add changed log for new title of task`() {
        // given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Auth Feature",
            state = "in progress",
            assignedTo = listOf(UUID.randomUUID()),
            createdBy = UUID.randomUUID(),
            projectId = UUID.randomUUID()
        )

        every { tasksRepository.updateTask(any()) } returns Unit

        editTaskTitleUseCase.invoke(taskId = task.id , newTitle = "School Library" )

        verify { logsRepository.addLog(any()) }

    }

}