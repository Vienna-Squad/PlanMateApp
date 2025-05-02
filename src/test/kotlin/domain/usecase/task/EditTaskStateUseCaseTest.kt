package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.utils.viewer.ExceptionViewer
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class EditTaskStateUseCaseTest {
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)

    private val dummyTask =
        Task(
            id = UUID.randomUUID().toString(),
            title = "Sample Task",
            state = "To Do",
            assignedTo = listOf("user1", "user2"),
            createdBy = "admin1",
            createdAt = LocalDateTime.now(),
            projectId = "project123"
        )


    @BeforeEach
    fun setup() {
        editTaskStateUseCase = EditTaskStateUseCase(
            tasksRepository,

            )
    }

    @Test
    fun `should edit task state when task exists`() {
        // given
        every { tasksRepository.get(dummyTask.id) } returns Result.success(dummyTask)
        // when
        editTaskStateUseCase(dummyTask.id, "In Progress")
        // then
        verify {
            tasksRepository.update(match {
                it.state == "In Progress" && it.id == dummyTask.id
            })
        }
    }

    @Test
    fun `should throw NoFoundException when task does not exist`() {
        // given
        every { tasksRepository.get(dummyTask.id) } returns Result.failure(NoFoundException())
        // when & then
        assertThrows<NoFoundException> {
            editTaskStateUseCase(dummyTask.id, "In Progress")
        }
    }

    @Test
    fun `should throw InvalidIdException when task id is blank`() {
        // given
        val exception = InvalidIdException()
        every { tasksRepository.get(" ") } throws exception
        // when & then
        val thrown = assertThrows<InvalidIdException> {
            editTaskStateUseCase(" ", "In Progress")
        }
        assertEquals(exception.message, thrown.message)
    }

    @Test
    fun `should not update task state if new state is the same as old state`() {
        // given
        every { tasksRepository.get(dummyTask.id) } returns Result.success(dummyTask)
        // when & then
        val thrown = assertThrows<InvalidIdException> {
            editTaskStateUseCase(dummyTask.id, dummyTask.state)
        }
        assertEquals("Task is already in the desired state", thrown.message)
    }
}