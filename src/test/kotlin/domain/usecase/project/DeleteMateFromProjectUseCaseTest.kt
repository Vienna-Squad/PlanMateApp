package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.ProjectHasNoThisMate
import org.example.domain.ProjectAccessDenied
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteMateFromProjectUseCaseTest {
    private lateinit var deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        deleteMateFromProjectUseCase = DeleteMateFromProjectUseCase(projectsRepository, logsRepository, usersRepository)
    }

    @Test
    fun `should remove mate and log when user is project creator and mate exists`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        //when
        deleteMateFromProjectUseCase(project.id, dummyMate.id)
        //then
        verify { projectsRepository.updateProject(match { !it.matesIds.contains(dummyMate.id) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not project creator`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<ProjectAccessDenied> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when mate is not in project`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        //when && then
        assertThrows<ProjectHasNoThisMate> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when project has no mates`() {
        //given
        val project = dummyProject.copy(matesIds = emptyList(), createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        //when && then
        assertThrows<ProjectHasNoThisMate> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
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
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getUserByID fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when updateProject fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        every { projectsRepository.updateProject(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        every { projectsRepository.updateProject(project.copy(matesIds = project.matesIds - dummyMate.id)) }
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }
}