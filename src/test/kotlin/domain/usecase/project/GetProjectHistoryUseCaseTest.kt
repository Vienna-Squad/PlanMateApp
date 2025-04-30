package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.domain.NoLogsFoundException
import org.example.domain.NoProjectFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectHistoryUseCaseTest {

    lateinit var projectsRepository: ProjectsRepository
    lateinit var getProjectHistoryUseCase: GetProjectHistoryUseCase
    lateinit var authRepository: AuthenticationRepository
    lateinit var logsRepository: LogsRepository

    val adminUser = User(username = "admin", "123", type = UserType.ADMIN)
    val mateUser = User(username = "mate", "5466", type = UserType.MATE)

    private val dummyProjects = listOf(
        Project(
            name = "E-Commerce Platform",
            states = listOf("Backlog", "In Progress", "Testing", "Completed"),
            createdBy = "admin1",
            matesIds = listOf("mate1", "mate2", "mate3")
        ),
        Project(
            name = "Social Media App",
            states = listOf("Idea", "Prototype", "Development", "Live"),
            createdBy = "admin2",
            matesIds = listOf("mate4", "mate5")
        ),
        Project(
            name = "Travel Booking System",
            states = listOf("Planned", "Building", "QA", "Release"),
            createdBy = "admin1",
            matesIds = listOf("mate1", "mate6")
        ),
    )

    private val dummyLogs = listOf(
        CreatedLog(
            username = "admin1",
            affectedId = dummyProjects[2].id,
            affectedType = Log.AffectedType.PROJECT
        ),
        DeletedLog(
            username = "admin1",
            affectedId = dummyProjects[0].id,
            affectedType = Log.AffectedType.PROJECT,
            deletedFrom = "E-Commerce Platform"
        ),
        ChangedLog(
            username = "admin1",
            affectedId = dummyProjects[0].id,
            affectedType = Log.AffectedType.PROJECT,
            oldValue = "In Progress",
            newValue = "Testing"
        )
    )


    @BeforeEach
    fun setUp() {
        projectsRepository = mockk()
        authRepository = mockk()
        logsRepository = mockk()
        getProjectHistoryUseCase = GetProjectHistoryUseCase(projectsRepository, authRepository, logsRepository)
    }

    @Test
    fun `should throw UnauthorizedException when user is not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        //when & then
        assertThrows<UnauthorizedException> {
            getProjectHistoryUseCase(dummyProjects[0].id)
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user is not admin`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(mateUser)

        //when & then
        assertThrows<UnauthorizedException> {
            getProjectHistoryUseCase(dummyProjects[0].id)
        }
    }
    @Test
    fun `should throw NoProjectFoundException when project not found`(){
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get("not-found-id")} returns Result.failure(NoProjectFoundException())

        //when &then
        assertThrows<NoProjectFoundException> {
            getProjectHistoryUseCase("not-found-id")
        }

    }

    @Test
    fun `should return list of logs when project history exists `() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
        every { logsRepository.getAll() } returns Result.success(listOf(dummyLogs[1],dummyLogs[2]))

        //when
        val history = getProjectHistoryUseCase(dummyProjects[0].id)

        //then
        assertEquals(2, history.size)

    }

    @Test
    fun `should throw exception when loading project history fails`() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
        every { logsRepository.getAll() } returns Result.failure(NoLogsFoundException())



        //when & then
        assertThrows<NoLogsFoundException> {
            getProjectHistoryUseCase(dummyProjects[0].id)
        }
    }

    @Test
    fun `should return empty list when no logs found for the project`() {
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.get(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
        every { logsRepository.getAll() } returns Result.success(emptyList())

        val result = getProjectHistoryUseCase(dummyProjects[0].id)

        assertTrue(result.isEmpty())
    }


}