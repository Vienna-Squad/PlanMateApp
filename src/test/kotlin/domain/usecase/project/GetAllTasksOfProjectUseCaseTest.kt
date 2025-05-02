package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.domain.InvalidIdException
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class GetAllTasksOfProjectUseCaseTest {

    private lateinit var getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        getAllTasksOfProjectUseCase =
            GetAllTasksOfProjectUseCase(tasksRepository, projectsRepository, authenticationRepository)
    }

    @Test
    fun `should return tasks that belong to given project ID for authorized user`() {
        // Given
        val projectId = "project-123"
        val user = createTestUser(id = "user-123")
        val project = createTestProject(id = projectId, matesIds = listOf(user.id))
        val task1 = createTestTask(title = "Task 1", projectId = projectId)
        val task2 = createTestTask(title = "Task 2", projectId = "project-321")
        val task3 = createTestTask(title = "Task 3", projectId = projectId)
        val allTasks = listOf(task1, task2, task3)

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { tasksRepository.getAll() } returns Result.success(allTasks)

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task1, task3)
    }

    @Test
    fun `should throw NoFoundException when project has no tasks`() {
        // Given
        val projectId = "project-123"
        val user = createTestUser(id = "user-123")
        val project = createTestProject(id = projectId, createdBy = user.id)
        val allTasks = listOf(
            createTestTask(title = "Task 1", projectId = "project-321"),
            createTestTask(title = "Task 2", projectId = "project-321")
        )

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { tasksRepository.getAll() } returns Result.success(allTasks)

        // When & Then
        assertThrows<NoFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw InvalidIdException when project does not exist`() {
        // Given
        val nonExistentProjectId = "non-existent-project"
        val user = createTestUser(id = "user-123")

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(nonExistentProjectId) } returns Result.failure(InvalidIdException())

        // When & Then
        assertThrows<InvalidIdException> {
            getAllTasksOfProjectUseCase(nonExistentProjectId)
        }
    }

    @Test
    fun `should throw NoFoundException when tasks repository fails`() {
        // Given
        val projectId = "project-123"
        val user = createTestUser(id = "user-123")
        val project = createTestProject(id = projectId, createdBy = user.id)

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { tasksRepository.getAll() } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user not found`() {
        // Given
        val projectId = "project-123"

        every { authenticationRepository.getCurrentUser() } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<UnauthorizedException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when user is not authorized`() {
        // Given
        val projectId = "project-123"
        val user = createTestUser(id = "user-999")
        val project = createTestProject(id = projectId, createdBy = "user-123", matesIds = listOf("user-456"))

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(projectId) } returns Result.success(project)

        // When & Then
        assertThrows<UnauthorizedException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should return tasks for admin project`() {
        // Given
        val projectId = "project-123"
        val user = createTestUser(id = "user-999", type = UserType.ADMIN)
        val project = createTestProject(id = projectId, createdBy = "user-123", matesIds = listOf("user-456"))
        val task1 = createTestTask(title = "Task 1", projectId = projectId)
        val task2 = createTestTask(title = "Task 2", projectId = projectId)

        every { authenticationRepository.getCurrentUser() } returns Result.success(user)
        every { projectsRepository.get(projectId) } returns Result.success(project)
        every { tasksRepository.getAll() } returns Result.success(listOf(task1, task2))

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task1, task2)
    }



    private fun createTestTask(
        title: String,
        state: String = "todo",
        assignedTo: List<String> = emptyList(),
        createdBy: String = "test-user",
        projectId: String
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
        id: String = "project-123",
        name: String = "Test Project",
        states: List<String> = emptyList(),
        createdBy: String = "test-user",
        matesIds: List<String> = emptyList()
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
        id: String = "user-123",
        username: String = "testUser",
        password: String = "hashed",
        type: UserType = UserType.MATE

    ): User {
        return User(
            id = id,
            username = username,
            password = password,
            type = type,
            cratedAt = LocalDateTime.now()
        )
    }
}