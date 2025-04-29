package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import org.example.domain.NoTaskFoundException
import org.example.domain.entity.Task
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditTaskTitleUseCaseTest {

    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    lateinit var editTaskTitleUseCase: EditTaskTitleUseCase

    @BeforeEach
    fun setUp() {
       editTaskTitleUseCase = EditTaskTitleUseCase(tasksRepository)
    }

    @Test
    fun `invoke should throw NoTaskFoundException if the task is not found in tasksRepository`() {
        // given
        val tasks = listOf(Task(
            title = "User Title",
            state = "in progress",
            assignedTo = listOf("3bnaser"),
            createdBy = "3bnaser",
            projectId = "12"
        ))
        every { tasksRepository.getAll() } returns Result.success(tasks)

        assertThrows <NoTaskFoundException>{ editTaskTitleUseCase.invoke("15","get the projects from repo") }

    }

}