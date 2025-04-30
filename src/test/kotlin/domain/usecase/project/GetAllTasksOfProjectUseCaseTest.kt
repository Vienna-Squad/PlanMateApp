package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NoFoundException
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.project.GetAllTasksOfProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class GetAllTasksOfProjectUseCaseTest {

    private lateinit var getAllTasksOfProjectUseCase: GetAllTasksOfProjectUseCase
    private val tasksRepository: TasksRepository = mockk()
    private val projectsRepository: ProjectsRepository = mockk()

    @BeforeEach
    fun setup() {
        getAllTasksOfProjectUseCase = GetAllTasksOfProjectUseCase(tasksRepository, projectsRepository)
    }

    @Test
    fun `should return tasks that belong to given project ID`() {
        // Given
        val projectId = "project-123"
        val task1 = createTestTask(title = "Task 1", projectId = projectId)
        val task2 = createTestTask(title = "Task 2", projectId = "project-321")
        val task3 = createTestTask(title = "Task 3", projectId = projectId)

        val allTasks = listOf(task1, task2, task3)

        every { projectsRepository.get(projectId) } returns Result.success(createTestProject(id = projectId))
        every { tasksRepository.getAll() } returns Result.success(allTasks)

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task1, task3)
    }

    @Test
    fun `should return single task that belong to given project ID`() {
        // Given
        val projectId = "project-123"
        val task = createTestTask(title = "Task 1", projectId = projectId)
        val otherTask = createTestTask(title = "Task 2", projectId = "project-321")

        every { projectsRepository.get(projectId) } returns Result.success(createTestProject(id = projectId))
        every { tasksRepository.getAll() } returns Result.success(listOf(task, otherTask))

        // When
        val result = getAllTasksOfProjectUseCase(projectId)

        // Then
        assertThat(result).containsExactly(task)
    }

    @Test
    fun `should throw NoFoundException when project has no tasks`() {
        // Given
        val projectId = "project-123"
        val allTasks = listOf(
            createTestTask(title = "Task 1", projectId = "project-321"),
            createTestTask(title = "Task 2", projectId = "project-321")
        )

        every { projectsRepository.get(projectId) } returns Result.success(createTestProject(id = projectId))
        every { tasksRepository.getAll() } returns Result.success(allTasks)

        // When & Then
        assertThrows<NoFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
    }

    @Test
    fun `should throw NoFoundException when project does not exist`() {
        // Given
        val nonExistentProjectId = "non-existent-project"
        every { projectsRepository.get(nonExistentProjectId) } returns Result.failure(
            NoFoundException()
        )

        // When & Then
        assertThrows<NoFoundException> {
            getAllTasksOfProjectUseCase(nonExistentProjectId)
        }
    }

    @Test
    fun `should throw NoFoundException when tasks repository fails`() {
        // Given
        val projectId = "project-123"
        every { projectsRepository.get(projectId) } returns Result.success(createTestProject(id = projectId))
        every { tasksRepository.getAll() } returns Result.failure(NoFoundException())

        // When & Then
        assertThrows<NoFoundException> {
            getAllTasksOfProjectUseCase(projectId)
        }
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
            cratedAt = LocalDateTime.now()
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
}