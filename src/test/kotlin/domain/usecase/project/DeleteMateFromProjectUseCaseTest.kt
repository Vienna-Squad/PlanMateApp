package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.entity.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteMateFromProjectUseCaseTest {
    private lateinit var deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        deleteMateFromProjectUseCase = DeleteMateFromProjectUseCase(projectsRepository, logsRepository,mockk(relaxed = true))
    }

    @Test
    fun `should delete mate from project and log when project has this mate`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(
            matesIds = dummyProject.matesIds + dummyMate.id,
            createdBy = dummyAdmin.id
        )
        //when
        deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        //then
        verify { projectsRepository.updateProject(match { !it.matesIds.contains(dummyMate.id) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw NotFoundException when project has no mates`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(matesIds = emptyList())
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw NotFoundException when project has no mate match passed id`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<NotFoundException> {
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
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(
            matesIds = dummyProject.matesIds + dummyMate.id,
        )
        every { projectsRepository.updateProject(match { it.id == dummyProject.id }) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }
}