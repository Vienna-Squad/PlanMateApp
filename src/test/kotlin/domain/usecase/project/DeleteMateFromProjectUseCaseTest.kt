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
        deleteMateFromProjectUseCase = DeleteMateFromProjectUseCase(projectsRepository, logsRepository)
    }

    @Test
    fun `should delete mate from project and log when project has this mate`() {
        //given
        val randomProject = dummyProject.copy(
            matesIds = dummyProject.matesIds + dummyMate.id,
            createdBy = dummyAdmin.id
        )
        every { projectsRepository.getProjectById(randomProject.id) } returns randomProject
        //when
        deleteMateFromProjectUseCase(randomProject.id, dummyMate.id)
        //then
        verify { projectsRepository.updateProject(match { !it.matesIds.contains(dummyMate.id) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw NotFoundException when project has no mates`() {
        //given
        val project = dummyProject.copy(matesIds = emptyList())
        every { projectsRepository.getProjectById(dummyProject.id) } returns project
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw NotFoundException when project has no mates match passed id`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }
}