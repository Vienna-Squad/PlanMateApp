package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
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

class EditProjectStatesUseCaseTest {
    private lateinit var editProjectStatesUseCase: EditProjectStatesUseCase
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val dummyProjects = listOf(
        Project(
            name = "Healthcare Management System",
            states = listOf("Planning", "Development", "Testing", "Deployment"),
            createdBy = "admin1",
            matesIds = listOf("mate1", "mate4", "mate5")
        ),
        Project(
            name = "Online Marketplace",
            states = listOf("Concept", "Design", "Implementation", "Launch"),
            createdBy = "admin2",
            matesIds = listOf("mate2", "mate6")
        ),
        Project(
            name = "Weather Forecast App",
            states = listOf("Research", "Prototype", "Development", "Release"),
            createdBy = "admin3",
            matesIds = listOf("mate3", "mate7")
        ),
        Project(
            name = "Music Streaming Service",
            states = listOf("Idea", "Development", "Testing", "Live"),
            createdBy = "admin4",
            matesIds = listOf("mate8", "mate9")
        ),
        Project(
            name = "AI Chatbot",
            states = listOf("Training", "Testing", "Deployment"),
            createdBy = "admin5",
            matesIds = listOf("mate10", "mate1")
        ),
        Project(
            name = "Virtual Reality Game",
            states = listOf("Concept", "Design", "Development", "Testing", "Release"),
            createdBy = "admin2",
            matesIds = listOf("mate2", "mate3")
        ),
        Project(
            name = "Smart Home System",
            states = listOf("Planning", "Implementation", "Testing", "Deployment"),
            createdBy = "admin3",
            matesIds = listOf("mate4", "mate5")
        ),
        Project(
            name = "Blockchain Payment System",
            states = listOf("Research", "Development", "Testing", "Launch"),
            createdBy = "admin4",
            matesIds = listOf("mate6", "mate7")
        ),
        Project(
            name = "E-Learning Platform",
            states = listOf("Draft", "Content Creation", "Review", "Published"),
            createdBy = "admin1",
            matesIds = listOf("mate8", "mate9")
        ),
        Project(
            name = "Ride Sharing App",
            states = listOf("Planning", "Development", "Testing", "Go Live"),
            createdBy = "admin5",
            matesIds = listOf("mate10", "mate2")
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
        every { projectsRepository.get(project.id) } returns Result.success(project)
        //when
        editProjectStatesUseCase(project.id, listOf("new state 1", "new state 2"))
        //then
        verify { logsRepository.add(match { it is ChangedLog }) }
    }

    @Test
    fun `should edit project states when project exists`() {
        //given
        val project = randomProject.copy(createdBy = dummyAdmin.id)
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(project.id) } returns Result.success(project)
        //when
        editProjectStatesUseCase(project.id, listOf("new state 1", "new state 2"))
        //then
        verify {
            projectsRepository.update(match {
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
            UnauthorizedException()
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
        every { projectsRepository.get(randomProject.id) } returns Result.success(randomProject)
        //when && then
        assertThrows<AccessDeniedException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project does not exist`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(randomProject.id) } returns Result.failure(NoFoundException())
        //when && then
        assertThrows<NoFoundException> {
            editProjectStatesUseCase(randomProject.id, listOf("new state 1", "new state 2"))
        }
    }

    @Test
    fun `should throw InvalidProjectIdException when project id is blank`() {
        //given
        every { authenticationRepository.getCurrentUser() } returns Result.success(dummyAdmin)
        every { projectsRepository.get(" ") } returns Result.failure(InvalidIdException())
        //when && then
        assertThrows<InvalidIdException> {
            editProjectStatesUseCase(" ", listOf("new state 1", "new state 2"))
        }
    }

}