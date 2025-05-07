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

    private lateinit var tasksRepository: TasksRepository
    private lateinit var getTaskUseCase: GetTaskUseCase

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        getTaskUseCase = GetTaskUseCase(tasksRepository)
    }

    @Test
    fun `should return task given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTasks[0].id) } returns dummyTasks[0]

        //when
        val result = getTaskUseCase(dummyTasks[0].id)

        //then
        assertTrue { result == dummyTasks[0] }
    }

    @Test
    fun `should throw Exception  when repo fails to fetch data task given task id`() {
        //Given
        every { tasksRepository.getTaskById(dummyTasks[0].id) } throws Exception()

        //when & then
        assertThrows<Exception> { getTaskUseCase(dummyTasks[0].id) }
    }


}

