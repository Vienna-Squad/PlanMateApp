package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.Log
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class DeleteTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var logsRepository: LogsRepository

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase


    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(
            tasksRepository,
            logsRepository,
        )
    }

    @Test
    fun `should delete project and add log when task exists`() {
        // Given
        every { tasksRepository.deleteTaskById(task.id) } returns Unit

        // When
        deleteTaskUseCase(task.id)
        // Then
        verify {
            tasksRepository.deleteTaskById(match {
                it == task.id
            })
        }
        verify {
            logsRepository.addLog(match {
                it.affectedId == task.id.toString() &&
                        it.affectedType == Log.AffectedType.TASK

            })
        }
    }


    @Test
    fun `should not log if task deletion fails`() {
        // Given
        every { tasksRepository.deleteTaskById(task.id) } throws Exception()

        // Then& When
        assertThrows<Exception> {
            tasksRepository.deleteTaskById(
                task.id
            )
        }
        verify(exactly = 0) {
            logsRepository.addLog(match {
                it.affectedId == task.id.toString()
                        &&
                        it.affectedType == Log.AffectedType.TASK


            })
        }
    }

}

private val user = User(
    id = UUID.randomUUID(),
    username = "adminUser",
    hashedPassword = "hashed",
    role = UserRole.ADMIN,
    cratedAt = LocalDateTime.now()
)


private val fixedProjectId = UUID.fromString("9f1602cc-87c0-4319-96b5-5d43766b9ae9") // consistent across test


private val task = Task(
    id = UUID.randomUUID(),
    title = "Task A",
    state = "todo",
    assignedTo = listOf(),
    createdBy = user.id,
    createdAt = LocalDateTime.now(),
    projectId = fixedProjectId
)
