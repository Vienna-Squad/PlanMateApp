package domain.usecase.task

import dummyTasks
import io.mockk.*
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteTaskUseCaseTest {
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val dummyTask = dummyTasks[0]

    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(
            tasksRepository,
            logsRepository,
            usersRepository
        )
    }

    @Test
    fun `should delete task and add log when task exists`() {
        // Given

        every { tasksRepository.deleteTaskById(dummyTask.id) } just Runs

        // When
        deleteTaskUseCase(dummyTask.id)
        // Then
        verify {
            tasksRepository.deleteTaskById(match {
                it == dummyTask.id
            })
        }
        verify {
            logsRepository.addLog(match {
                it is DeletedLog &&
                        it.affectedId == dummyTask.id.toString() &&
                        it.affectedType == Log.AffectedType.TASK

            })
        }
    }


    @Test
    fun `should not log if task deletion fails`() {
        // Given
        every { tasksRepository.deleteTaskById(dummyTask.id) } throws Exception()

        // Then& When
        assertThrows<Exception> {
            tasksRepository.deleteTaskById(
                dummyTask.id
            )
        }
        verify(exactly = 0) {
            logsRepository.addLog(
                match {
                    it is DeletedLog &&
                    it.affectedId == dummyTask.id.toString() &&
                    it.affectedType == Log.AffectedType.TASK


                })
        }
    }

}
