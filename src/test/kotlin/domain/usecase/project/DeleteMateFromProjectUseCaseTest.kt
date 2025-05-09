package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyProject
import io.mockk.every
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
    fun `should delete mate from project and log when project has this mate`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id, createdBy = dummyAdmin.id)
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        deleteMateFromProjectUseCase(project.id, dummyMate.id)
        //then
        verify { projectsRepository.updateProject(match { !it.matesIds.contains(dummyMate.id) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw ProjectHasNoException when project has no mates`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(matesIds = emptyList())
        //when && then
        assertThrows<ProjectHasNoException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw ProjectHasNoException when project has no mate match passed id`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<ProjectHasNoException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should not log or update if project retrieval fails`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should not log if mate deletion fails`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        every { usersRepository.getUserByID(dummyMate.id) } returns dummyMate
        every { projectsRepository.getProjectById(dummyProject.id) } returns project
        every { projectsRepository.updateProject(match { it.id == dummyProject.id }) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }
}