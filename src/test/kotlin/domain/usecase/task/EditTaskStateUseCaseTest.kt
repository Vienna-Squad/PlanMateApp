package domain.usecase.task

import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.ChangedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditTaskStateUseCaseTest {
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val dummyTask = dummyTasks[0]

    @BeforeEach
    fun setup() {
        editTaskStateUseCase = EditTaskStateUseCase(
            tasksRepository,
            logsRepository,
            usersRepository
        )
    }

    @Test
    fun `should edit task state when task exists`() {
        // Given
        every { tasksRepository.getTaskById(dummyTask.id) } returns (dummyTask)

        // When
        editTaskStateUseCase(dummyTask.id, "In Progress")

        // Then
        verify {
            tasksRepository.updateTask(match {
                it.state == "In Progress" && it.id == dummyTask.id
            })
        }
        verify {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }

    @Test
    fun `should throw an Exception and not log when getTaskById fails `() {
        // Given

        every { tasksRepository.getTaskById(dummyTask.id) } throws Exception()

        // when&Then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyTask.id, "In Progress")
        }
        verify(exactly = 0) {
            tasksRepository.updateTask(match {
                it.id == dummyTask.id
            })
        }
        verify(exactly = 0) {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }

    @Test
    fun `should throw an Exception and not log when updateTask fails `() {
        // Given
        every { tasksRepository.updateTask(any()) } throws Exception()
        // when&Then
        assertThrows<Exception> {
            editTaskStateUseCase(dummyTask.id, "In Progress")
        }

        verify(exactly = 0) {
            logsRepository.addLog(
                match
                {
                    it is ChangedLog
                })
        }
    }


}

