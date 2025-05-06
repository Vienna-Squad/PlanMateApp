package domain.usecase.project

import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.entity.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteStateFromProjectUseCaseTest {

    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        deleteStateFromProjectUseCase = DeleteStateFromProjectUseCase(projectsRepository, logsRepository)
    }

    @Test
    fun `should delete state when project has it`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(states = listOf("test", "done"))
        //when
        deleteStateFromProjectUseCase.invoke(dummyProject.id, "test")
        //then
        verify { projectsRepository.updateProject(match { !it.states.contains("test") }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }


    @Test
    fun `should throw NotFoundException state when project has no this state`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(states = listOf("done"))
        //when && then
        assertThrows<NotFoundException> { deleteStateFromProjectUseCase.invoke(dummyProject.id, "test") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw NotFoundException state when project has no any states`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(states = emptyList())
        //when && then
        assertThrows<NotFoundException> { deleteStateFromProjectUseCase.invoke(dummyProject.id, "test") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should not update or log when project retrieval fails`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(dummyProject.id, "test") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should not log when project update fails`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(states = listOf("test", "done"))
        every { projectsRepository.updateProject(match { !it.states.contains("test") }) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(dummyProject.id, "test") }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }
}
