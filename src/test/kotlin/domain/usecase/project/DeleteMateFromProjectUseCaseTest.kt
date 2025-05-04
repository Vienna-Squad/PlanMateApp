package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.DeleteMateFromProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class DeleteMateFromProjectUseCaseTest {
    private lateinit var deleteMateFromProjectUseCase: DeleteMateFromProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)
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
    private val dummyProject = dummyProjects[5]
    private val dummyAdmin = User(
        username = "admin1",
        hashedPassword = "adminPass123",
        role = UserRole.ADMIN
    )
    private val dummyMate = User(
        username = "mate1",
        hashedPassword = "matePass456",
        role = UserRole.MATE
    )


    @BeforeEach
    fun setup() {
        deleteMateFromProjectUseCase = DeleteMateFromProjectUseCase(
            projectsRepository,
            logsRepository,
            authRepository
        )
    }

    @Test
    fun `should delete mate from project and log when mate and project are exist`() {
        //given
        val randomProject = dummyProject.copy(
            matesIds = dummyProject.matesIds + listOf(dummyMate.id),
            createdBy = dummyAdmin.id
        )
        every { authRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(randomProject.id) } returns Result.success(randomProject)
        every { authRepository.getUserByID(dummyMate.id) } returns Result.success(dummyMate)
        //when
        deleteMateFromProjectUseCase(randomProject.id, dummyMate.id)
        //then
        verify { projectsRepository.updateProject(match { !it.matesIds.contains(dummyMate.id) }) }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw UnauthorizedException when no logged in user found`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<UnauthorizedException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is mate`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(dummyMate)
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<AccessDeniedException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user has not this project`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<AccessDeniedException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project does not exist`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.failure(NotFoundException(""))
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw NoMateFoundException when project has not this mate`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.success(dummyProject.copy(createdBy = dummyAdmin.id))
        every { authRepository.getUserByID(dummyMate.id) } returns Result.success(dummyMate)
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }

    @Test
    fun `should throw NoMateFoundException when no mate has this id`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(dummyProject.id) } returns Result.success(dummyProject.copy(createdBy = dummyAdmin.id))
        every { authRepository.getUserByID(dummyMate.id) } returns Result.failure(NotFoundException(""))
        //when && then
        assertThrows<NotFoundException> {
            deleteMateFromProjectUseCase(dummyProject.id, dummyMate.id)
        }
    }
}