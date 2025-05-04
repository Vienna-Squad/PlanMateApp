package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.domain.InvalidIdException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class GetAllTasksOfProjectUseCaseTest {

    private lateinit var getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllTasksOfProjectUseCase =
            GetAllTasksOfProjectUseCase(tasksRepository, projectsRepository, authRepository)
    }

    @Test
    fun `should return tasks that belong to given project ID for authorized user`() {
        // Given
        val projectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID())
        val project = createTestProject(id = UUID.randomUUID(), matesIds = listOf(user.id))
        val task1 = createTestTask(title = "Task 1", projectId = UUID.randomUUID())
        val task2 = createTestTask(title = "Task 2", projectId = UUID.randomUUID())
        val task3 = createTestTask(title = "Task 3", projectId = UUID.randomUUID())
        val allTasks = listOf(task1, task2, task3)

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { tasksRepository.getAllTasks() } returns Result.success(allTasks)

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task1, task3)
    }

    @Test
    fun `should throw NoFoundException when project has no tasks`() {
        // Given
        val projectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID())
        val project = createTestProject(id = projectId, createdBy = user.id)
        val allTasks = listOf(
            createTestTask(title = "Task 1", projectId = projectId),
            createTestTask(title = "Task 2", projectId = projectId)
        )

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { tasksRepository.getAllTasks() } returns Result.success(allTasks)

        // When & Then
        assertThrows<NotFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw InvalidIdException when project does not exist`() {
        // Given
        val nonExistentProjectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID())

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(nonExistentProjectId) } returns Result.failure(InvalidIdException(""))

        // When & Then
        assertThrows<InvalidIdException> {
            getAllTasksOfProjectUseCase(nonExistentProjectId)
        }
    }

    @Test
    fun `should throw NoFoundException when tasks repository fails`() {
        // Given
        val projectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID())
        val project = createTestProject(id = projectId, createdBy = user.id)

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { tasksRepository.getAllTasks() } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user not found`() {
        // Given
        val projectId = UUID.randomUUID()

        every { authRepository.getCurrentUser() } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<UnauthorizedException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when user is not authorized`() {
        // Given
        val projectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID())
        val project = createTestProject(id = projectId, createdBy = UUID.randomUUID(), matesIds = listOf("user-456").map { UUID.fromString(it) })

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)

        // When & Then
        assertThrows<UnauthorizedException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should return tasks for admin project`() {
        // Given
        val projectId = UUID.randomUUID()
        val user = createTestUser(id = UUID.randomUUID(), type = UserRole.ADMIN)
        val project = createTestProject(id = projectId, createdBy = UUID.randomUUID(), matesIds = listOf("user-456").map { UUID.fromString(it) })
        val task1 = createTestTask(title = "Task 1", projectId = projectId)
        val task2 = createTestTask(title = "Task 2", projectId = projectId)

        every { authRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { tasksRepository.getAllTasks() } returns Result.success(listOf(task1, task2))

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task1, task2)
    }



    private fun createTestTask(
        title: String,
        state: String = "todo",
        assignedTo: List<UUID> = emptyList(),
        createdBy: UUID = UUID.fromString("test-user"),
        projectId: UUID
    ): Task {
        return Task(
            title = title,
            state = state,
            assignedTo = assignedTo,
            createdBy = createdBy,
            projectId = projectId,
            createdAt = LocalDateTime.now()
        )
    }

    private fun createTestProject(
        id: UUID= UUID.fromString("project-123"),
        name: String = "Test Project",
        states: List<String> = emptyList(),
        createdBy: UUID = UUID.fromString("test-user"),
        matesIds: List<UUID> = emptyList()
    ): Project {
        return Project(
            id = id,
            name = name,
            states = states,
            createdBy = createdBy,
            cratedAt = LocalDateTime.now(),
            matesIds = matesIds
        )
    }

    private fun createTestUser(
        id: UUID = UUID.fromString("user-123"),
        username: String = "testUser",
        password: String = "hashed",
        type: UserRole = UserRole.MATE

    ): User {
        return User(
            id = id,
            username = username,
            hashedPassword = password,
            role = type,
            cratedAt = LocalDateTime.now()
        )
    }
}