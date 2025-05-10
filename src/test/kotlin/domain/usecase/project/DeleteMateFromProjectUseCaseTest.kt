package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyProject
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.example.domain.AccessDeniedException

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
    fun `should project creator can delete mate from project and log when project has this mate`() {
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
    fun `should throw AccessDeniedException when the currentUser is not the project creator`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<AccessDeniedException> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when the project has no this mate`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        //when && then
        assertThrows<ProjectHasNoException> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
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
        assertThrows<ProjectHasNoException> { deleteMateFromProjectUseCase(project.id, dummyMate.id) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not update the project or log if getCurrentUser fails`() {
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
    fun `should not update the project or log if getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not update the project or log if getUserByID fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not log if updateProject fails`() {
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
    fun `should use case throw exception if any function fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        every { projectsRepository.updateProject(match{ !it.matesIds.contains(dummyMate.id) }) } just Runs
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }
}