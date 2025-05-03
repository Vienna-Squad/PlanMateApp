//package domain.usecase.project
//
//import io.mockk.every
//import io.mockk.mockk
//import org.example.domain.AccessDeniedException
//import org.example.domain.FailedToCallLogException
//import org.example.domain.NoFoundException
//import org.example.domain.UnauthorizedException
//import org.example.domain.entity.*
//import org.example.domain.repository.AuthenticationRepository
//import org.example.domain.repository.LogsRepository
//import org.example.domain.repository.ProjectsRepository
//import org.example.domain.usecase.project.GetProjectHistoryUseCase
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class GetProjectHistoryUseCaseTest {
//
//    lateinit var projectsRepository: ProjectsRepository
//    lateinit var getProjectHistoryUseCase: GetProjectHistoryUseCase
//    lateinit var authRepository: AuthenticationRepository
//    lateinit var logsRepository: LogsRepository
//
//    val adminUser = User(username = "admin", hashedPassword = "123", type = UserType.ADMIN)
//    val mateUser = User(username = "mate", hashedPassword = "5466", type = UserType.MATE)
//
//    private val dummyProjects = listOf(
//        Project(
//            name = "E-Commerce Platform",
//            states = listOf("Backlog", "In Progress", "Testing", "Completed"),
//            createdBy = adminUser.id,
//            matesIds = listOf(mateUser.id, "mate2", "mate3")
//        ),
//        Project(
//            name = "Social Media App",
//            states = listOf("Idea", "Prototype", "Development", "Live"),
//            createdBy = adminUser.id,
//            matesIds = listOf("mate4", "mate5")
//        ),
//        Project(
//            name = "Travel Booking System",
//            states = listOf("Planned", "Building", "QA", "Release"),
//            createdBy = adminUser.id,
//            matesIds = listOf("mate1", "mate6")
//        ),
//    )
//
//    private val dummyLogs = listOf(
//        CreatedLog(
//            username = "admin1",
//            affectedId = dummyProjects[2].id,
//            affectedType = Log.AffectedType.PROJECT
//        ),
//        DeletedLog(
//            username = "admin1",
//            affectedId = dummyProjects[0].id,
//            affectedType = Log.AffectedType.PROJECT,
//            deletedFrom = "E-Commerce Platform"
//        ),
//        ChangedLog(
//            username = "admin1",
//            affectedId = dummyProjects[0].id,
//            affectedType = Log.AffectedType.PROJECT,
//            changedFrom = "In Progress",
//            changedTo = "Testing"
//        )
//    )
//
//
//    @BeforeEach
//    fun setUp() {
//        projectsRepository = mockk()
//        authRepository = mockk()
//        logsRepository = mockk()
//        getProjectHistoryUseCase = GetProjectHistoryUseCase(projectsRepository, authRepository, logsRepository)
//    }
//
//    @Test
//    fun `should throw UnauthorizedException when user is not logged in`() {
//        //given
//        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())
//
//        //when & then
//        assertThrows<UnauthorizedException> {
//            getProjectHistoryUseCase(dummyProjects[0].id)
//        }
//    }
//
//    @Test
//    fun `should throw AccessDeniedException when current user is admin but not owner of the project`() {
//        //given
//        val newAdmin = adminUser.copy(id = "new-id")
//        every { authRepository.getCurrentUser() } returns Result.success(newAdmin)
//        every { projectsRepository.get(dummyProjects[2].id) } returns Result.success(dummyProjects[2])
//
//        //when & then
//        assertThrows<AccessDeniedException> {
//            getProjectHistoryUseCase(dummyProjects[2].id)
//        }
//    }
//
//    @Test
//    fun `should throw AccessDeniedException when current user is mate but not belong to project`() {
//        //given
//        every { authRepository.getCurrentUser() } returns Result.success(mateUser)
//        every { projectsRepository.get(dummyProjects[1].id) } returns Result.success(dummyProjects[1])
//
//        //when & then
//        assertThrows<AccessDeniedException> {
//            getProjectHistoryUseCase(dummyProjects[1].id)
//        }
//    }
//
//    @Test
//    fun `should throw NoProjectFoundException when project not found`() {
//        // given
//        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
//        every { projectsRepository.get("not-found-id") } returns Result.failure(NoFoundException())
//
//        //when &then
//        assertThrows<NoFoundException> {
//            getProjectHistoryUseCase("not-found-id")
//        }
//
//    }
//
//    @Test
//    fun `should return list of logs when project history exists `() {
//        // given
//        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
//        every { projectsRepository.get(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
//        every { logsRepository.getAll() } returns Result.success(dummyLogs)
//
//        //when
//        val history = getProjectHistoryUseCase(dummyProjects[0].id)
//
//        //then
//        assertEquals(2, history.size)
//
//    }
//
//    @Test
//    fun `should throw FailedToAddLogException when loading project history fails`() {
//        // given
//        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
//        every { projectsRepository.get(dummyProjects[0].id) } returns Result.success(dummyProjects[0])
//        every { logsRepository.getAll() } returns Result.failure(FailedToCallLogException())
//
//        //when & then
//        assertThrows<FailedToCallLogException> {
//            getProjectHistoryUseCase(dummyProjects[0].id)
//        }
//    }
//
//}