package domain.usecase.project

import dummyAdmin
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.exceptions.NoChangeException
import org.example.domain.exceptions.ProjectAccessDeniedException
import org.example.domain.entity.log.ChangedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.project.EditProjectNameUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditProjectNameUseCaseTest {
    private lateinit var editProjectNameUseCase: EditProjectNameUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editProjectNameUseCase = EditProjectNameUseCase(projectsRepository, logsRepository, usersRepository,Validator)
    }

    @Test
    fun `should edit project name and log when user is creator and project exists`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        editProjectNameUseCase(project.id, "new name")
        //then
        verify { projectsRepository.updateProject(match { it.name == "new name" }) }
        verify { logsRepository.addLog(match { it is ChangedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not the creator`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<ProjectAccessDeniedException> { editProjectNameUseCase(dummyProject.id, "new name") }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw NoChangeException when new name is exact same old name`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<NoChangeException> { editProjectNameUseCase(project.id, project.name) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw NoChangeException when new name is same old name but has extra spaces`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<NoChangeException> { editProjectNameUseCase(project.id, " ${project.name} ") }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> {
            editProjectNameUseCase(dummyProject.id, "new name")
        }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            editProjectNameUseCase(dummyProject.id, "new name")
        }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when updateProject fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.updateProject(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            editProjectNameUseCase(project.id, "new name")
        }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.updateProject(project.copy(name = "new name")) }
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            editProjectNameUseCase(project.id, "new name")
        }
    }
}