package domain.usecase.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.UsersRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class AddMateToTaskUseCaseTest {

    private lateinit var addMateToTaskUseCase: AddMateToTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        addMateToTaskUseCase = AddMateToTaskUseCase(
            tasksRepository,
            logsRepository,
            usersRepository,
            projectsRepository
        )
    }
    @Test
    fun `should add mate to task and log the action successfully is creator`() {
        // Given
        val taskId = UUID.randomUUID()  // Random UUID
        val mateId = UUID.randomUUID()  // Random UUID
        val projectId = UUID.randomUUID()  // Random UUID
        val creatorId = UUID.randomUUID()  // Random UUID

        val currentUser = createTestUser(id = creatorId, username = "creator")
        val task = createTestTask(id = taskId, createdBy = currentUser.id, assignedTo = emptyList(), projectId = projectId)
        val mate = createTestUser(id = mateId)
        val project = createTestProject(id = projectId, createdBy = currentUser.id, matesIds = listOf(mateId))
        val updatedTask = task.copy(assignedTo = listOf(mateId))

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.success(task)
        every { usersRepository.getUserByID(mateId) } returns Result.success(mate)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)

        // When
        addMateToTaskUseCase(taskId, mateId)

        // Then
        verify { tasksRepository.updateTask(updatedTask) }
        verify { logsRepository.addLog(any<AddedLog>()) }
        assertThat(updatedTask.assignedTo).containsExactly(mateId)
    }

    @Test
    fun `should add mate to task when user is admin`() {
        // Given
        val taskId = UUID.randomUUID()  // Random UUID
        val mateId = UUID.randomUUID()  // Random UUID
        val projectId = UUID.randomUUID()  // Random UUID
        val creatorId = UUID.randomUUID()  // Random UUID
        val adminId = UUID.randomUUID()  // Random UUID

        val currentUser = createTestUser(id = adminId, username = "admin", type = UserRole.ADMIN)
        val task = createTestTask(id = taskId, createdBy = creatorId, assignedTo = emptyList(), projectId = projectId)
        val mate = createTestUser(id = mateId)
        val project = createTestProject(id = projectId, createdBy = creatorId, matesIds = listOf(mateId))
        val updatedTask = task.copy(assignedTo = listOf(mateId))

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.success(task)
        every { usersRepository.getUserByID(mateId) } returns Result.success(mate)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)

        // When
        addMateToTaskUseCase(taskId, mateId)

        // Then
        verify { tasksRepository.updateTask(updatedTask) }
        verify { logsRepository.addLog(any<AddedLog>()) }
        assertThat(updatedTask.assignedTo).containsExactly(mateId)
    }

    @Test
    fun `should add mate to task when user is already assigned to task`() {
        // Given
        val taskId = UUID.randomUUID()  // Random UUID
        val mateId = UUID.randomUUID()  // Random UUID
        val projectId = UUID.randomUUID()  // Random UUID
        val creatorId = UUID.randomUUID()  // Random UUID
        val currentUserId = UUID.randomUUID()  // Random UUID

        val currentUser = createTestUser(id = currentUserId, username = "mate")
        val task = createTestTask(
            id = taskId,
            createdBy = creatorId,
            assignedTo = listOf(currentUserId),
            projectId = projectId
        )
        val mate = createTestUser(id = mateId)
        val project = createTestProject(id = projectId, createdBy = creatorId, matesIds = listOf(mateId))
        val updatedTask = task.copy(assignedTo = listOf(currentUserId, mateId))

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.success(task)
        every { usersRepository.getUserByID(mateId) } returns Result.success(mate)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)

        // When
        addMateToTaskUseCase(taskId, mateId)

        // Then
        verify { tasksRepository.updateTask(updatedTask) }
        verify { logsRepository.addLog(any<AddedLog>()) }
        assertThat(updatedTask.assignedTo).containsExactly(currentUserId, mateId)
    }

    @Test
    fun `should throw UnauthorizedException when user is not admin, creator, or mate`() {
        // Given
        val taskId = UUID.randomUUID()  // Random UUID
        val mateId = UUID.randomUUID()  // Random UUID
        val projectId = UUID.randomUUID()  // Random UUID
        val creatorId = UUID.randomUUID()  // Random UUID
        val unrelatedUserId = UUID.randomUUID()  // Random UUID
        val assignedMateId = UUID.randomUUID()  // Random UUID

        val currentUser = createTestUser(id = unrelatedUserId, type = UserRole.MATE)
        val task = createTestTask(
            id = taskId,
            createdBy = creatorId,
            assignedTo = listOf(assignedMateId),
            projectId = projectId
        )
        val mate = createTestUser(id = mateId)
        val project = createTestProject(id = projectId, createdBy = creatorId, matesIds = listOf(mateId))

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.success(task)
        every { usersRepository.getUserByID(mateId) } returns Result.success(mate)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)

        // When & Then
        assertThrows<UnauthorizedException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw InvalidIdException when task does not exist`() {
        // Given
        val taskId = UUID.randomUUID()  // Random UUID
        val mateId = UUID.randomUUID()  // Random UUID
        val currentUser = createTestUser(id = UUID.randomUUID())  // Random UUID

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw NotFoundException when task update fails`() {
        // Given
        val taskId = UUID.randomUUID()
        val mateId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val currentUser = createTestUser()
        val task = createTestTask(id = taskId, createdBy = currentUser.id, assignedTo = emptyList(), projectId = projectId)
        val mate = createTestUser(id = mateId)
        val project = createTestProject(id = projectId, matesIds = listOf(mateId))
        val updatedTask = task.copy(assignedTo = listOf(mateId))

        every { usersRepository.getCurrentUser() } returns Result.success(currentUser)
        every { tasksRepository.getTaskById(taskId) } returns Result.success(task)
        every { usersRepository.getUserByID(mateId) } returns Result.success(mate)
        every { projectsRepository.getProjectById(projectId) } returns Result.success(project)
        every { tasksRepository.updateTask(updatedTask) } returns Result.failure(NotFoundException(""))

        // When & Then
        assertThrows<NotFoundException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user not found`() {
        // Given
        val taskId = UUID.randomUUID()
        val mateId = UUID.randomUUID()

        // Mocking the failure when fetching the current user
        every { usersRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

        // When & Then
        assertThrows<UnauthorizedException> {
            addMateToTaskUseCase(taskId, mateId)
        }
    }

private fun createTestTask(
    id: UUID = UUID.randomUUID(),
    title: String = "Test Task",
    state: String = "todo",
    assignedTo: List<UUID> = emptyList(),
    createdBy: UUID = UUID.randomUUID(),
    projectId: UUID = UUID.randomUUID()
): Task {
    return Task(
        id = id,
        title = title,
        state = state,
        assignedTo = assignedTo,
        createdBy = createdBy,
        projectId = projectId,
        createdAt = LocalDateTime.now()
    )
}

    private fun createTestUser(
        id: UUID = UUID.randomUUID(),
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

    private fun createTestProject(
        id: UUID = UUID.randomUUID(),
        name: String = "Test Project",
        states: List<String> = emptyList(),
        createdBy: UUID = UUID.randomUUID(),
        matesIds: List<UUID> = emptyList()
    ): Project {
        return Project(
            id = id,
            name = name,
            states = states,
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            matesIds = matesIds
        )
    }
}
