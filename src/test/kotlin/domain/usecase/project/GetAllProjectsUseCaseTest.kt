package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyAdmin
import dummyProjects
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.GetAllProjectsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAllProjectsUseCaseTest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllProjectsUseCase = GetAllProjectsUseCase(projectsRepository, usersRepository)
    }

    @Test
    fun `should return projects created by current user when user logged in`() {
        //given
        val projects = dummyProjects + listOf(
            dummyProjects.random().copy(createdBy = dummyAdmin.id),
            dummyProjects.random().copy(createdBy = dummyAdmin.id),
            dummyProjects.random().copy(createdBy = dummyAdmin.id),
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getAllProjects() } returns projects.shuffled()
        //when
        val filteredProjects = getAllProjectsUseCase()
        //then
        assertThat(filteredProjects.all { it.createdBy == dummyAdmin.id }).isTrue()
    }

    @Test
    fun `should throw NotFoundException when user has no projects`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getAllProjects() } returns dummyProjects
        //when && then
        assertThrows<NotFoundException> { getAllProjectsUseCase() }
    }

    @Test
    fun `should throw NotFoundException when all projects list is empty`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getAllProjects() } returns emptyList()
        //when && then
        assertThrows<NotFoundException> { getAllProjectsUseCase() }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { getAllProjectsUseCase() }
        verify(exactly = 0) { projectsRepository.getAllProjects() }
    }

    @Test
    fun `should not proceed when getAllProjects fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getAllProjects() } throws Exception()
        //when && then
        assertThrows<Exception> { getAllProjectsUseCase() }
    }
}