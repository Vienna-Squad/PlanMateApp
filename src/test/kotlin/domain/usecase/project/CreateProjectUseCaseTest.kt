package domain.usecase.project

import dummyAdmin
import dummyMate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.exceptions.FeatureAccessDeniedException
import org.example.domain.entity.log.CreatedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class CreateProjectUseCaseTest {
    lateinit var createProjectUseCase: CreateProjectUseCase
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        createProjectUseCase = CreateProjectUseCase(projectRepository, usersRepository, logsRepository,Validator)
    }

    @Test
    fun `should create project and log when admin creates a project`() {
        //given
        val newProjectName = "new project name"
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        //when
        createProjectUseCase(newProjectName)
        //then
        verify { projectRepository.addProject(match { it.name == newProjectName && it.createdBy == dummyAdmin.id }) }
        verify { logsRepository.addLog(match { it is CreatedLog }) }
    }

    @Test
    fun `should throw FeatureAccessDeniedException when mate tries to create project`() {
        //given
        val newProjectName = "new project name"
        every { usersRepository.getCurrentUser() } returns dummyMate
        //when && then
        assertThrows<FeatureAccessDeniedException> { createProjectUseCase(newProjectName) }
        verify(exactly = 0) { projectRepository.addProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        val newProjectName = "new project name"
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { createProjectUseCase(newProjectName) }
        verify(exactly = 0) { projectRepository.addProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addProject fails`() {
        //given
        val newProjectName = "new project name"
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectRepository.addProject(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { createProjectUseCase(newProjectName) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val newProjectName = "new project name"
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { createProjectUseCase(newProjectName) }
    }
}