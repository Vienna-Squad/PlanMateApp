package domain.usecase.task

import dummyAdmin
import dummyProject
import dummyTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.TaskAccessDeniedException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.task.GetTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue


class GetTaskUseCaseTest {

    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private lateinit var getTaskUseCase: GetTaskUseCase


    @BeforeEach
    fun setup() {
        getTaskUseCase = GetTaskUseCase(tasksRepository, usersRepository, projectsRepository,Validator)
    }

    @Test
    fun `should return task given task id`() {
        //Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        val result = getTaskUseCase(task.id)

        //then
        assertTrue { result.id == task.id }
    }

    @Test
    fun `should not complete execution when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { getTaskUseCase(dummyTask.id) }
        verify(exactly = 0) { tasksRepository.getTaskById(any()) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
    }

    @Test
    fun `should not complete execution when getTaskById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { getTaskUseCase(dummyAdmin.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
    }

    @Test
    fun `should not complete execution when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyAdmin.id) } returns dummyTask
        every { projectsRepository.getProjectById(dummyTask.projectId) } throws Exception()
        //when && then
        assertThrows<Exception> { getTaskUseCase(dummyAdmin.id) }
    }

    @Test
    fun `should throw TaskAccessDeniedException when user is not owner or mate in the project `() {
        //given
        val task = dummyTask
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns dummyProject
        //when && then
        assertThrows<TaskAccessDeniedException> { getTaskUseCase(task.id) }
    }

}

