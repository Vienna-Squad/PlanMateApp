package domain.usecase.task

import dummyMate
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteMateFromTaskUseCaseTest {


    lateinit var deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    private val dummyTask = dummyTasks[0]
    @BeforeEach
    fun setUp() {
        deleteMateFromTaskUseCase = DeleteMateFromTaskUseCase(tasksRepository, logsRepository, usersRepository  )

    }

    @Test
    fun `should delete mate when given task id and mate id`() {
        //Given
        val dummyMates = dummyTask.assignedTo
        every { tasksRepository.getTaskById(dummyTask.id) } returns dummyTask
        // When
        deleteMateFromTaskUseCase(dummyTask.id, dummyMates[0])
        //Then
        verify {
            tasksRepository.updateTask(match {
                !
                (it.assignedTo.contains(dummyMates[0]))
            })
        }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw Exception when tasksRepository getTaskById throw Exception given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTask.id) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTask.id, dummyMate.id)
        }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }

    }

    @Test
    fun `should throw Exception when tasksRepository updateTask throw Exception given task id`() {
        //Given
        val task = dummyTask.copy(assignedTo = dummyTask.assignedTo + dummyMate.id)
        every { tasksRepository.getTaskById(task.id) } returns  task
        every { tasksRepository.updateTask(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTask.id, dummyMate.id)
        }
        verify (exactly = 0){ logsRepository.addLog(match { it is DeletedLog }) }

    }

    @Test
    fun `should throw Exception when addLog fails `() {
        //Given
        val task = dummyTask.copy(assignedTo = dummyTask.assignedTo + dummyMate.id)
        every { tasksRepository.getTaskById(task.id) } returns  task
        every { logsRepository.addLog(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(dummyTask.id, dummyMate.id)
        }

    }
}
