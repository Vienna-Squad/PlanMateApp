package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.FailedToAddLogException
import org.example.domain.FailedToCreateProject
import org.example.domain.UnauthorizedException
import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {


    lateinit var projectRepository: ProjectsRepository
    lateinit var createProjectUseCase: CreateProjectUseCase
    lateinit var authRepository: AuthenticationRepository
    lateinit var logsRepository: LogsRepository

    val name = "graduation project"
    val states = listOf("done", "in-progress", "todo")
    val createdBy = "20"
    val matesIds = listOf("1", "2", "3", "4", "5")

    val newProject = Project(name = name, states = states, createdBy = createdBy, matesIds = matesIds)

    val adminUser = User(username = "admin", hashedPassword = "123", type = UserType.ADMIN)
    val mateUser = User(username = "mate", hashedPassword = "5466", type = UserType.MATE)

    @BeforeEach
    fun setUp() {

        projectRepository = mockk(relaxed = true)
        authRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        createProjectUseCase = CreateProjectUseCase(projectRepository, authRepository, logsRepository)

    }

    @Test
    fun `should throw UnauthorizedException when user is not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        //when & then
        assertThrows<UnauthorizedException> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }
    }

    @Test
    fun `should throw AccessDeniedException when current user is not admin`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(mateUser)

        //when & then
        assertThrows<AccessDeniedException> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }
    }
    @Test
    fun `should add project when current user is admin and data is valid`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)

        // when
        createProjectUseCase(name, states, createdBy, matesIds)

        // then
        verify {
            projectRepository.add(match {
                it.name == name &&
                        it.states == states &&
                        it.createdBy == createdBy &&
                        it.matesIds == matesIds
            })
        }
    }

    @Test
    fun `should throw FailedToCreateProject when project addition fails`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { projectRepository.add(any()) } returns Result.failure(FailedToCreateProject())

        //when & then
        assertThrows<FailedToCreateProject> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }
    }

    @Test
    fun `should log project creation when user is admin and added project successfully`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)

        // when
        createProjectUseCase(name, states, createdBy, matesIds)

        // then
        verify {
            logsRepository.add(
                match {
                    it is CreatedLog
                }
            )
        }
    }

    @Test
    fun `should throw FailedToAddLogException when logging the project creation fails`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { logsRepository.add(any()) } returns Result.failure(FailedToAddLogException())

        //when & then
        assertThrows<FailedToAddLogException> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }
    }


}