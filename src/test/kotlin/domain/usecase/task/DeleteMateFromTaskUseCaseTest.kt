package domain.usecase.task

import dummyMate
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteMateFromTaskUseCaseTest {

    lateinit var tasksRepository: TasksRepository
    lateinit var deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase
    lateinit var logsRepository: LogsRepository

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        deleteMateFromTaskUseCase = DeleteMateFromTaskUseCase(tasksRepository, logsRepository)
    }

    @Test
    fun `should delete mate when given task id and mate id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTasks[0].id) } returns dummyTasks[0]
        // When
        deleteMateFromTaskUseCase(dummyTasks[0].id, dummyTasks[0].assignedTo[0])
        //Then
        verify {
            tasksRepository.updateTask(match {
                !
                (it.assignedTo.contains(dummyTasks[0].assignedTo[0]))
            })
        }
    }

    @Test
    fun `should throw Exception when tasksRepository getTaskById throw Exception given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTasks[0].id) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTasks[0].id, dummyMate.id)
        }

    }

    @Test
    fun `should throw Exception when tasksRepository updateTask throw Exception given task id`() {
        //Given
        every { tasksRepository.updateTask(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTasks[0].id, dummyMate.id)
        }

    }

    @Test
    fun `should throw Exception when addLog fails `() {
        //Given
        every { logsRepository.addLog(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTasks[0].id, dummyMate.id)
        }

    }
}
