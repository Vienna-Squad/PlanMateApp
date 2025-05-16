package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyAdmin
import dummyLogs
import dummyMate
import dummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.LogsNotFoundException
import org.example.domain.ProjectAccessDeniedException
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetProjectHistoryUseCaseTest {

    private lateinit var getProjectHistoryUseCase: GetProjectHistoryUseCase
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getProjectHistoryUseCase =
            GetProjectHistoryUseCase(logsRepository, projectsRepository, usersRepository, Validator)
    }

    @Test
    fun `should retrieve all logs of project when project creator retrieves logs`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val projectLogs = listOf(
            CreatedLog(
                username = "admin1",
                affectedId = project.id,
                affectedName = "P-101",
                affectedType = Log.AffectedType.PROJECT
            ), AddedLog(
                username = "admin1",
                affectedId = UUID.randomUUID(),
                affectedName = "P-102",
                affectedType = Log.AffectedType.STATE,
                addedTo = "project-${project.id}"
            )
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.getAllLogs() } returns dummyLogs + projectLogs
        //when
        val filteredLogs = getProjectHistoryUseCase(project.id)
        //then
        assertThat(filteredLogs.all {
            it.affectedId == project.id || it.toString().contains(project.id.toString())
        }).isTrue()
    }

    @Test
    fun `should retrieve all logs of project when project mate retrieves logs`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val projectLogs = listOf(
            CreatedLog(
                username = "admin1",
                affectedId = project.id,
                affectedName = "P-101",
                affectedType = Log.AffectedType.PROJECT
            ), AddedLog(
                username = "admin1",
                affectedId = UUID.randomUUID(),
                affectedName = "P-102",
                affectedType = Log.AffectedType.STATE,
                addedTo = "project-${project.id}"
            )
        )
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.getAllLogs() } returns dummyLogs + projectLogs
        //when
        val filteredLogs = getProjectHistoryUseCase(project.id)
        //then
        assertThat(filteredLogs.all {
            it.affectedId == project.id || it.toString().contains(project.id.toString())
        }).isTrue()
    }

    @Test
    fun `should throw ProjectAccessDenied when user is not project creator or mate`() {
        //given
        val projectLogs = listOf(
            CreatedLog(
                username = "admin1",
                affectedId = dummyProject.id,
                affectedName = "P-101",
                affectedType = Log.AffectedType.PROJECT
            ), AddedLog(
                username = "admin1",
                affectedId = UUID.randomUUID(),
                affectedName = "P-102",
                affectedType = Log.AffectedType.STATE,
                addedTo = "project-${dummyProject.id}"
            )
        )
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        every { logsRepository.getAllLogs() } returns dummyLogs + projectLogs
        //when && then
        assertThrows<ProjectAccessDeniedException> { getProjectHistoryUseCase(dummyProject.id) }
    }

    @Test
    fun `should throw LogsNotFound when filtered logs list is empty`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.getAllLogs() } returns dummyLogs
        //when && when
        assertThrows<LogsNotFoundException> { getProjectHistoryUseCase(project.id) }
    }

    @Test
    fun `should throw LogsNotFound when all logs list is empty`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.getAllLogs() } returns emptyList()
        //when && when
        assertThrows<LogsNotFoundException> { getProjectHistoryUseCase(project.id) }
    }

    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { getProjectHistoryUseCase(dummyAdmin.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { logsRepository.getAllLogs() }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } throws Exception()
        //when && then
        assertThrows<Exception> { getProjectHistoryUseCase(project.id) }
        verify(exactly = 0) { logsRepository.getAllLogs() }
    }

    @Test
    fun `should not proceed when getAllLogs fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.getAllLogs() } throws Exception()
        //when && then
        assertThrows<Exception> { getProjectHistoryUseCase(project.id) }
    }
}
