package domain.usecase.project

import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProjectUseCaseTest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        deleteProjectUseCase = DeleteProjectUseCase(
            projectsRepository,
            logsRepository,
            mockk(relaxed = true)
        )
    }

    @Test
    fun `should delete project and add log when project exists`() {
        //given
        every { projectsRepository.deleteProjectById(dummyProject.id) } returns Unit
        //when
        deleteProjectUseCase(dummyProject.id)
        //then
        verify { projectsRepository.deleteProjectById(match { it == dummyProject.id }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should not log if project deletion fails`() {
        //given
        every { projectsRepository.deleteProjectById(dummyProject.id) } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteProjectUseCase(dummyProject.id)
        }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }
}
