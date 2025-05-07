package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.UsersRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach


class CreateProjectUseCaseTest {


    lateinit var projectRepository: ProjectsRepository
    lateinit var createProjectUseCase: CreateProjectUseCase
    lateinit var usersRepository: UsersRepository
    lateinit var logsRepository: LogsRepository

    val name = "graduation project"
    val createdBy = "20"

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        createProjectUseCase = CreateProjectUseCase(projectRepository, usersRepository, logsRepository)
    }

    @Test
    fun `should not complete creation of project when current user is null`() {
        // given
        every { usersRepository.getCurrentUser() } returns null

        createProjectUseCase.invoke(name = name)

        verify(exactly = 0) {  projectRepository.addProject(any()) }
    }


    @Test
    fun `should call getCurrentUser`() {
        // when
        createProjectUseCase.invoke(name = name)
        // then
        verify { usersRepository.getCurrentUser() }
    }

    @Test
    fun `should call add project`() {
        // when
        createProjectUseCase.invoke(name = name)
        // then
        verify { projectRepository.addProject(any()) }
    }

    @Test
    fun `should add created log`() {
        // when
        createProjectUseCase.invoke(name = name)
        // then
        verify { logsRepository.addLog(any()) }
    }

}