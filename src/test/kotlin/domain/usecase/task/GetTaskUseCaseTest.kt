package domain.usecase.task

import dummyTasks
import io.mockk.every
import io.mockk.mockk
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.GetTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue


class GetTaskUseCaseTest {

    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private lateinit var getTaskUseCase: GetTaskUseCase
    private val dummyTask=dummyTasks[0]
    @BeforeEach
    fun setup() {
        getTaskUseCase = GetTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return task given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTask.id) } returns dummyTask

        //when
        val result = getTaskUseCase(dummyTask.id)

        //then
        assertTrue { result.id == dummyTask.id }
    }

    @Test
    fun `should throw Exception  when repo fails to fetch data task given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTask.id) } throws Exception()

        //when & then
        assertThrows<Exception> { getTaskUseCase(dummyTask.id) }
    }


}

