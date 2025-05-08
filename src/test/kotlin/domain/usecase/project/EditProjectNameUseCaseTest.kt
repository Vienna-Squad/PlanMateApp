package domain.usecase.project

import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NoChangeException
import org.example.domain.entity.ChangedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.EditProjectNameUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EditProjectNameUseCaseTest {
    private lateinit var editProjectNameUseCase: EditProjectNameUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editProjectNameUseCase = EditProjectNameUseCase(projectsRepository, logsRepository,mockk(relaxed = true))
    }

    @Test
    fun `should edit project name and add log when project exists`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when
        editProjectNameUseCase(dummyProject.id, "new name")
        //then
        verify { projectsRepository.updateProject(match { it.name == "new name" }) }
        verify { logsRepository.addLog(match { it is ChangedLog }) }
    }

    @Test
    fun `should throw NoChangeException when the new name and project name are same`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject.copy(name = "dummy project")
        //when && then
        assertThrows<NoChangeException> { editProjectNameUseCase(dummyProject.id, "dummy project") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is ChangedLog }) }
    }

    @Test
    fun `should not update or log when project retrieval fails`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } throws Exception()
        //when && then
        assertThrows<Exception> { editProjectNameUseCase(dummyProject.id, "new name") }
        verify(exactly = 0) { projectsRepository.updateProject(match { it.id == dummyProject.id }) }
        verify(exactly = 0) { logsRepository.addLog(match { it is ChangedLog }) }
    }

    @Test
    fun `should not log when project update fails`() {
        //given
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        every { projectsRepository.updateProject(dummyProject.copy(name = "new name")) } throws Exception()
        //when && then
        assertThrows<Exception> { editProjectNameUseCase(dummyProject.id, "new name") }
        verify(exactly = 0) { logsRepository.addLog(match { it is ChangedLog }) }
    }
}
