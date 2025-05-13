package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyAdmin
import dummyMate
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.ProjectAccessDenied
import org.example.domain.TaskNotInProjectException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAllTasksOfProjectUseCaseTest {

    private lateinit var getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllTasksOfProjectUseCase = GetAllTasksOfProjectUseCase(tasksRepository, projectsRepository, usersRepository)
    }

    @Test
    fun `should return tasks when project creator retrieves tasks`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val tasks = dummyTasks + listOf(
            dummyTasks.random().copy(projectId = project.id),
            dummyTasks.random().copy(projectId = project.id),
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.getAllTasks() } returns tasks
        //when
        val filteredTasks = getAllTasksOfProjectUseCase(project.id)
        //then
        assertThat(filteredTasks.all { it.projectId == project.id }).isTrue()
    }

    @Test
    fun `should return tasks when project mate retrieves tasks`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val tasks = dummyTasks + listOf(
            dummyTasks.random().copy(projectId = project.id),
            dummyTasks.random().copy(projectId = project.id),
        )
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.getAllTasks() } returns tasks
        //when
        val filteredTasks = getAllTasksOfProjectUseCase(project.id)
        //then
        assertThat(filteredTasks.all { it.projectId == project.id }).isTrue()
    }

    @Test
    fun `should throw AccessDeniedException when user is not related to its project`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<ProjectAccessDenied> { getAllTasksOfProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { tasksRepository.getAllTasks() }
    }

    @Test
    fun `should throw TaskNotInProjectException when project has no tasks`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.getAllTasks() } returns dummyTasks
        //when && then
        assertThrows<TaskNotInProjectException> { getAllTasksOfProjectUseCase(project.id) }
    }

    @Test
    fun `should throw TaskNotInProjectException when all tasks list is empty`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.getAllTasks() } returns emptyList()
        //when && then
        assertThrows<TaskNotInProjectException> { getAllTasksOfProjectUseCase(project.id) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { getAllTasksOfProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.getAllTasks() }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } throws Exception()
        //when && then
        assertThrows<Exception> { getAllTasksOfProjectUseCase(project.id) }
        verify(exactly = 0) { tasksRepository.getAllTasks() }

    }

    @Test
    fun `should not proceed when getAllTasks fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.getAllTasks() } throws Exception()
        //when && then
        assertThrows<Exception> { getAllTasksOfProjectUseCase(project.id) }
    }
}