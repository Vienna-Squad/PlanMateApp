package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.UnauthorizedException
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException

class DeleteProjectUseCaseTest {
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
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
        Project(
            name = "Food Delivery App",
            states = listOf("Todo", "In Progress", "Review", "Delivered"),
            createdBy = "admin3",
            matesIds = listOf("mate7", "mate8")
        ),
        Project(
            name = "Online Education Platform",
            states = listOf("Draft", "Content Ready", "Published"),
            createdBy = "admin2",
            matesIds = listOf("mate2", "mate9")
        ),
        Project(
            name = "Banking Mobile App",
            states = listOf("Requirements", "Design", "Development", "Testing", "Deployment"),
            createdBy = "admin4",
            matesIds = listOf("mate10", "mate3")
        ),
        Project(
            name = "Fitness Tracking App",
            states = listOf("Planned", "In Progress", "Completed"),
            createdBy = "admin1",
            matesIds = listOf("mate5", "mate7")
        ),
        Project(
            name = "Event Management System",
            states = listOf("Initiated", "Planning", "Execution", "Closure"),
            createdBy = "admin5",
            matesIds = listOf("mate8", "mate9")
        ),
        Project(
            name = "Online Grocery Store",
            states = listOf("Todo", "Picking", "Dispatch", "Delivered"),
            createdBy = "admin3",
            matesIds = listOf("mate1", "mate4")
        ),
        Project(
            name = "Real Estate Listing Site",
            states = listOf("Listing", "Viewing", "Negotiation", "Sold"),
            createdBy = "admin4",
            matesIds = listOf("mate6", "mate10")
        )
    )
    private val dummyProject = dummyProjects[5]
    private val dummyAdmin = User(
        username = "admin1",
        password = "adminPass123",
        type = UserType.ADMIN
    )
    private val dummyMate = User(
        username = "mate1",
        password = "matePass456",
        type = UserType.MATE
    )


    @BeforeEach
    fun setup() {
        deleteProjectUseCase = DeleteProjectUseCase(
            projectsRepository,
            logsRepository,
            authenticationRepository
        )
    }

    @Test
    fun `should delete project and add log when project exists`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(dummyProject.id) } returns Result.success(dummyProject.copy(createdBy = dummyAdmin.id))
        //when
        deleteProjectUseCase(dummyProject.id)
        //then
        verify { projectsRepository.delete(any()) }
        verify { logsRepository.add(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw UnauthorizedException when no logged in user found`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())
        every { projectsRepository.get(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<UnauthorizedException> {
            deleteProjectUseCase(dummyProject.id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is mate`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyMate)
        every { projectsRepository.get(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<AccessDeniedException> {
            deleteProjectUseCase(dummyProject.id)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user has not this project`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(dummyProject.id) } returns Result.success(dummyProject)
        //when && then
        assertThrows<AccessDeniedException> {
            deleteProjectUseCase(dummyProject.id)
        }
    }

    @Test
    fun `should throw NoProjectFoundException when project does not exist`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(dummyProject.id) } returns Result.failure(NoFoundException())
        //when && then
        assertThrows<NoFoundException> {
            deleteProjectUseCase(dummyProject.id)
        }
    }

    @Test
    fun `should throw InvalidProjectIdException when project id is blank`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(" ") } returns Result.failure(InvalidIdException())
        //when && then
        assertThrows<InvalidIdException> {
            deleteProjectUseCase(" ")
        }
    }
}