package domain.usecase.task



import io.mockk.every
import io.mockk.mockk
import org.example.domain.FailedToAddException
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteTaskUseCaseTest {

    private val tasksRepository: TasksRepository = mockk()
    private val deleteTaskUseCase = DeleteTaskUseCase(tasksRepository)

    @Test
    fun `invoke should succeed when task is deleted successfully`() {
        // given
        val taskId = "task123"
        every { tasksRepository.delete(taskId) } returns Result.success(Unit)

        // when
        val result = deleteTaskUseCase.invoke(taskId)

        // then
        assertEquals(Unit, result) // returns Unit → no exception
    }

    @Test
    fun `invoke should throw FailedToAddException when task deletion fails`() {
        // given
        val taskId = "task123"
        every { tasksRepository.delete(taskId) } returns Result.failure(FailedToAddException())

        // when & then
        assertThrows<FailedToAddException> {
            deleteTaskUseCase.invoke(taskId)
        }
    }
}
