package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.*
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateTaskUseCaseTest {
    private lateinit var tasksRepository: TasksRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        projectsRepository = mockk(relaxed = true)
        authenticationRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(
            tasksRepository,
            logsRepository,
            projectsRepository,
            authenticationRepository
        )
    }

    @Test
    fun `should throw UnauthorizedException when no logged-in user is found`() {
        // Given
        every { authenticationRepository.getCurrentUser() } returns Result.failure(Exception())

        // When & Then
        assertThrows<UnauthorizedException> {
            createTaskUseCase(createTask())
        }
    }

    @Test
    fun `should throw NoFoundException when project is not found`() {
        // Given
        val task = createTask()
        val user = createUser()
        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.failure(Exception())

        // When & Then
        assertThrows<NoFoundException> {
            createTaskUseCase(task)
        }
    }

    @Test
    fun `should throw AccessDeniedException when user is not in matesIds`() {
        // Given
        val user = createUser().copy(id = "15")
        val project = createProject(createdBy = "999").copy(matesIds = listOf("20", "21"))
        val task = createTask()

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.success(project)

        // When & Then
        assertThrows<AccessDeniedException> {
            createTaskUseCase(task)
        }
    }

    @Test
    fun `should throw AccessDeniedException when project createdBy is not current user`() {
        // Given
        val task = createTask()
        val user = createUser().copy(id = "13")
        val project = createProject(createdBy = "999")

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.success(project)

        // When & Then
        assertThrows<AccessDeniedException> {
            createTaskUseCase(task)
        }
    }

    @Test
    fun `should throw FailedToAddException when task addition fails`() {
        // Given
        val user = createUser().copy(id = "12")
        val project = createProject(createdBy = "12").copy(matesIds = listOf("12"))
        val task = createTask().copy(createdBy = "12")

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.success(project)
        every { tasksRepository.add(task) } returns Result.failure(Exception())
        // When & Then
        assertThrows<FailedToAddException> {
            createTaskUseCase(task)
        }
    }

    @Test
    fun `should throw FailedToLogException when logging creation fails`() {
        // Given
        val user = createUser().copy(id = "12")
        val project = createProject(createdBy = "12").copy(matesIds = listOf("12"))
        val task = createTask().copy(createdBy = "12")

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.success(project)
        every { tasksRepository.add(task) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.failure(Exception("Log error"))

        // When & Then
        assertThrows<FailedToLogException> {
            createTaskUseCase(task)
        }
    }

    @Test
    fun `should add task and log creation in logs repository`() {
        // Given
        val user = createUser()
        val project = createProject(user.id)
        val task = createTask()

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(task.projectId) } returns Result.success(project)
        every { tasksRepository.add(task) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.success(Unit)

        // When
        createTaskUseCase(task)

        // Then
        verify { tasksRepository.add(task) }
        verify {
            logsRepository.add(match {
                it.username == user.username &&
                        it.affectedId == task.id &&
                        it.affectedType == Log.AffectedType.TASK
            })
        }
    }

    private fun createTask(): Task {
        return Task(
            title = " A Task",
            state = "in progress",
            assignedTo = listOf("12", "123"),
            createdBy = "12",
            projectId = "999"
        )
    }

    private fun createProject(createdBy:String): Project {
        return Project(
            id = "999",
            name = "Test Project",
            createdBy = createdBy,
            states = emptyList(),
            matesIds = emptyList()
        )
    }

    private fun createUser(): User {
        return User(
            username = "firstuser",
            hashedPassword = "1234",
            type = UserType.MATE
        )
    }
}
