package domain.usecase.project

import dummyAdmin
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.exceptions.ProjectAccessDeniedException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProjectUseCaseTest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        deleteProjectUseCase = DeleteProjectUseCase(
            projectsRepository,
            logsRepository,
            usersRepository
        )
    }

    @Test
    fun `should delete project and log when user is creator`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns project
        //when
        deleteProjectUseCase(dummyProject.id)
        //then
        verify { projectsRepository.deleteProjectById(match { it == dummyProject.id }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not project creator`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<ProjectAccessDeniedException> { deleteProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.deleteProjectById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { deleteProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { projectsRepository.deleteProjectById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.deleteProjectById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when deleteProjectById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.deleteProjectById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteProjectUseCase(dummyProject.id) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.deleteProjectById(project.id) }
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteProjectUseCase(dummyProject.id) }
    }
}
