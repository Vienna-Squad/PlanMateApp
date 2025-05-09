package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyAdmin
import dummyProject
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
    fun `should retrieve user projects when user logged in`() {
        //given
        every { projectsRepository.getAllProjects() } returns dummyProjects + dummyProjects.random()
            .copy(createdBy = dummyAdmin.id) + dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        //when
        val projects = getAllProjectsUseCase()
        //then
        assertThat(projects.size).isEqualTo(2)
        assertThat(projects.all { it.createdBy == dummyAdmin.id }).isTrue()
    }

    @Test
    fun `should throw NotFoundException when user not have any project`() {
        //given
        every { projectsRepository.getAllProjects() } returns dummyProjects
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        //when && then
        assertThrows<NotFoundException> { getAllProjectsUseCase() }
    }

    @Test
    fun `should throw Exception when getAllProjects fails`() {
        //given
        every { projectsRepository.getAllProjects() } throws Exception()
        //when && then
        assertThrows<Exception> { getAllProjectsUseCase() }
        verify(exactly = 0) { usersRepository.getCurrentUser() }
    }
}