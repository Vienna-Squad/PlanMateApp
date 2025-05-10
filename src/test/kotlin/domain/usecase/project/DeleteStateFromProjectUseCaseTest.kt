package domain.usecase.project

import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.State
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.DeleteStateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteStateFromProjectUseCaseTest {

    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        deleteStateFromProjectUseCase =
            DeleteStateFromProjectUseCase(projectsRepository, logsRepository, mockk(relaxed = true))
    }

    @Test
    fun `should delete state when project has it`() {
        //given
        val project = dummyProject.copy(states = listOf("test", "done").map { State(name = it) })
        every { projectsRepository.getProjectById(dummyProject.id) } returns project
        //when
        deleteStateFromProjectUseCase.invoke(project.id, "test")
        //then
        verify { projectsRepository.updateProject(match { it.states.all { state -> state.name != "test" } }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }


    @Test
    fun `should throw ProjectHasNoException state when project has no this state`() {
        //given
        val project = dummyProject.copy(states = listOf("done").map { State(name = it) })
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<ProjectHasNoException> { deleteStateFromProjectUseCase(project.id, "test") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == project.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw ProjectHasNoException state when project has no any states`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(states = emptyList())
        //when && then
        assertThrows<ProjectHasNoException> { deleteStateFromProjectUseCase.invoke(dummyProject.id, "test") }
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
        val project = dummyProject.copy(states = listOf("test", "done").map { State(name = it) })
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.updateProject(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(project.id, "test") }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }
    }
}
