package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*
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

    private val dummyTask = Task(
        id = UUID.randomUUID(),
        title = "Sample Task",
        state = "To Do",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        createdAt = LocalDateTime.now(),
        projectId = UUID.randomUUID()
    )

    @BeforeEach
    fun setup() {
        editTaskStateUseCase = EditTaskStateUseCase(
            tasksRepository
        )
    }

    @Test
    fun `should edit task state when task exists`() {
        // Given
        every { tasksRepository.getTaskById(dummyTask.id) } returns Result.success(dummyTask)

        // When
        editTaskStateUseCase(dummyTask.id, "In Progress")

        // Then
        verify {
            tasksRepository.updateTask(match {
                it.state == "In Progress" && it.id == dummyTask.id // Using random UUID comparison
            })
        }
    }

    @Test
    fun `should throw NoFoundException when task does not exist`() {
        // Given
        every { tasksRepository.getTaskById(dummyTask.id) } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            editTaskStateUseCase(dummyTask.id, "In Progress")
        }
    }

    @Test
    fun `should throw InvalidIdException when task id is blank`() {
        // Given
        val exception = InvalidIdException("")
        every { tasksRepository.getTaskById(any()) } throws exception // Allow any UUID for invalid id

        // When & Then
        val thrown = assertThrows<InvalidIdException> {
            editTaskStateUseCase(UUID.randomUUID(), "In Progress") // Use random UUID
        }
        assertEquals(exception.message, thrown.message)
    }
}