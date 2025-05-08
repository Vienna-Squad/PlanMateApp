package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAllTasksOfProjectUseCaseTest {

    private lateinit var getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllTasksOfProjectUseCase = GetAllTasksOfProjectUseCase(tasksRepository)
    }

    @Test
    fun `should return tasks of project when existed project has tasks`() {
        //given
        every { tasksRepository.getAllTasks() } returns dummyTasks + dummyTasks.random()
            .copy(projectId = dummyProject.id) + dummyTasks.random()
            .copy(projectId = dummyProject.id) + dummyTasks.random().copy(projectId = dummyProject.id)
        //when
        val tasks = getAllTasksOfProjectUseCase(dummyProject.id)
        //then
        assertThat(tasks.size).isEqualTo(3)
        assertThat(tasks.all { it.projectId == dummyProject.id }).isTrue()
    }

    @Test
    fun `should throw NotFoundException when existed project has no tasks`() {
        //given
        every { tasksRepository.getAllTasks() } returns dummyTasks
        //when && then
        assertThrows<NotFoundException> { getAllTasksOfProjectUseCase(dummyProject.id) }
    }
}