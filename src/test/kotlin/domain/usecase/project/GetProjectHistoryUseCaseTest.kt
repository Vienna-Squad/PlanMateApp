package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.domain.AccessDeniedException
import org.example.domain.FailedToCallLogException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetProjectHistoryUseCaseTest {

    lateinit var projectsRepository: ProjectsRepository
    lateinit var getProjectHistoryUseCase: GetProjectHistoryUseCase
    lateinit var authRepository: AuthRepository
    lateinit var logsRepository: LogsRepository

    val adminUser = User(username = "admin", hashedPassword = "123", role = UserRole.ADMIN)
    val mateUser = User(username = "mate", hashedPassword = "5466", role = UserRole.MATE)

    private val dummyProjects = listOf(
        Project(
            name = "E-Commerce Platform",
            states = listOf("Backlog", "In Progress", "Testing", "Completed"),
            createdBy = UUID.fromString("admin1"),
            matesIds = listOf("mate1", "mate2", "mate3").map { UUID.fromString(it) }
        ),
        Project(
            name = "Social Media App",
            states = listOf("Idea", "Prototype", "Development", "Live"),
            createdBy = UUID.fromString("admin2"),
            matesIds = listOf("mate4", "mate5").map { UUID.fromString(it) }
        ),
        Project(
            name = "Travel Booking System",
            states = listOf("Planned", "Building", "QA", "Release"),
            createdBy = UUID.fromString("admin2"),
            matesIds = listOf("mate1", "mate6").map { UUID.fromString(it) }
        ),
        Project(
            name = "Food Delivery App",
            states = listOf("Todo", "In Progress", "Review", "Delivered"),
            createdBy = UUID.fromString("admin3"),
            matesIds = listOf("mate7", "mate8").map { UUID.fromString(it) }
        ),
        Project(
            name = "Online Education Platform",
            states = listOf("Draft", "Content Ready", "Published"),
            createdBy = UUID.fromString("admin2"),
            matesIds = listOf("mate2", "mate9").map { UUID.fromString(it) }
        ),
        Project(
            name = "Banking Mobile App",
            states = listOf("Requirements", "Design", "Development", "Testing", "Deployment"),
            createdBy = UUID.fromString("admin4"),
            matesIds = listOf("mate10", "mate3").map { UUID.fromString(it) }
        ),
        Project(
            name = "Fitness Tracking App",
            states = listOf("Planned", "In Progress", "Completed"),
            createdBy = UUID.fromString("admin1"),
            matesIds = listOf("mate5", "mate7").map { UUID.fromString(it) }
        ),
        Project(
            name = "Event Management System",
            states = listOf("Initiated", "Planning", "Execution", "Closure"),
            createdBy = UUID.fromString("admin5"),
            matesIds = listOf("mate8", "mate9").map { UUID.fromString(it) }
        ),
        Project(
            name = "Online Grocery Store",
            states = listOf("Todo", "Picking", "Dispatch", "Delivered"),
            createdBy = UUID.fromString("admin3"),
            matesIds = listOf("mate1", "mate4").map { UUID.fromString(it) }
        ),
        Project(
            name = "Real Estate Listing Site",
            states = listOf("Listing", "Viewing", "Negotiation", "Sold"),
            createdBy = UUID.fromString("admin4"),
            matesIds = listOf("mate6", "mate10").map { UUID.fromString(it) }
        )
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
            changedFrom = "In Progress",
            changedTo = "Testing"
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
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

        //when & then
        assertThrows<UnauthorizedException> {
            getProjectHistoryUseCase(dummyProjects[0].id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when current user is admin but not owner of the project`() {
        //given
        val newAdmin = adminUser.copy(id = UUID.randomUUID())
        every { authRepository.getCurrentUser() } returns Result.success(newAdmin)
        every { projectsRepository.getProjectById(dummyProjects[2].id) } returns Result.success(dummyProjects[2])

        //when & then
        assertThrows<AccessDeniedException> {
            getProjectHistoryUseCase(dummyProjects[2].id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when current user is mate but not belong to project`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(mateUser)
        every { projectsRepository.getProjectById(dummyProjects[1].id) } returns Result.success(dummyProjects[1])

        //when & then
        assertThrows<AccessDeniedException> {
            getProjectHistoryUseCase(dummyProjects[1].id)
        }
    }

    @Test
    fun `should throw NoProjectFoundException when project not found`() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(UUID.fromString("not-found-id")) } returns Result.failure(NotFoundException(""))

        //when &then
        assertThrows<NotFoundException> {
            getProjectHistoryUseCase(UUID.fromString("not-found-id"))
        }

    }

    @Test
    fun `should return list of logs when project history exists `() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
        every { logsRepository.getAllLogs() } returns Result.success(dummyLogs)

        //when
        val history = getProjectHistoryUseCase(dummyProjects[0].id)

        //then
        assertEquals(2, history.size)

    }

    @Test
    fun `should throw FailedToAddLogException when loading project history fails`() {
        // given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectsRepository.getProjectById(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
        every { logsRepository.getAllLogs() } returns Result.failure(FailedToCallLogException(""))

        //when & then
        assertThrows<FailedToCallLogException> {
            getProjectHistoryUseCase(dummyProjects[0].id)
        }
    }

}