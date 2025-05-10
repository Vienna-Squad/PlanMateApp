package domain.usecase.project

import dummyAdmin
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.DeleteStateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteStateFromProjectUseCaseTest {
    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        deleteStateFromProjectUseCase =
            DeleteStateFromProjectUseCase(projectsRepository, logsRepository, usersRepository)
    }

    @Test
    fun `should delete state when user is creator and state exists`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val state = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        deleteStateFromProjectUseCase.invoke(project.id, state.name)
        //then
        verify { projectsRepository.updateProject(match { !it.states.contains(state) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not project creator`() {
        //given
        val state = dummyProject.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<AccessDeniedException> { deleteStateFromProjectUseCase.invoke(dummyProject.id, state.name) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when state not found in project`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<ProjectHasNoException> { deleteStateFromProjectUseCase.invoke(project.id, "state") }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val state = project.states.random()
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> {
            deleteStateFromProjectUseCase.invoke(project.id, state.name)
        }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val state = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(project.id, state.name) }
        verify(exactly = 0) { projectsRepository.updateProject(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when updateProject fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val state = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.updateProject(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(project.id, state.name) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val state = project.states.random()
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { projectsRepository.updateProject(project.copy(states = project.states - state)) }
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteStateFromProjectUseCase.invoke(project.id, state.name) }
    }
}
