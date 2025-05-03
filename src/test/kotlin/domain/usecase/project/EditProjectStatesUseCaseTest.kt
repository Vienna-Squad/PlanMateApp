package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.example.domain.repository.LogsRepository
import java.util.UUID

class EditProjectStatesUseCaseTest {
    private lateinit var editProjectStatesUseCase: EditProjectStatesUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
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
    private val randomProject = dummyProjects[5]
    private val dummyAdmin = User(
        username = "admin1",
        hashedPassword = "adminPass123",
        type = UserType.ADMIN
    )
    private val dummyMate = User(
        username = "mate1",
        hashedPassword = "matePass456",
        type = UserType.MATE
    )


    @BeforeEach
    fun setup() {
        editProjectStatesUseCase = EditProjectStatesUseCase(
            projectsRepository,
            logsRepository,
            authenticationRepository
        )
    }

    @Test
    fun `should add ChangedLog when project states are updated`() {
        //given
        val project = randomProject.copy(createdBy = dummyAdmin.id)
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(project.id) } returns Result.success(project)
        //when
        editProjectStatesUseCase(project.id, listOf("new state 1", "new state 2"))
        //then
        verify { logsRepository.addLog(match { it is ChangedLog }) }
    }

    @Test
    fun `should edit project states when project exists`() {
        //given
        val project = randomProject.copy(createdBy = dummyAdmin.id)
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(project.id) } returns Result.success(project)
        //when
        editProjectStatesUseCase(project.id, listOf("new state 1", "new state 2"))
        //then
        verify {
            projectsRepository.updateProject(match {
                it.states == listOf(
                    "new state 1",
                    "new state 2"
                )
            })
        }
    }

    @Test
    fun `should throw UnauthorizedException when no logged in user found`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(
            UnauthorizedException("")
        )
        //when && then
        assertThrows<UnauthorizedException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is mate`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyMate)
        //when && then
        assertThrows<AccessDeniedException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }

    @Test
    fun `should throw AccessDeniedException when user has not this project`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(randomProject.id) } returns Result.success(randomProject)
        //when && then
        assertThrows<AccessDeniedException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project does not exist`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.getProjectById(randomProject.id) } returns Result.failure(NotFoundException(""))
        //when && then
        assertThrows<NotFoundException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }


}