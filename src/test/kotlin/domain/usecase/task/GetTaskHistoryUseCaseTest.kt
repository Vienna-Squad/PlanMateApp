package domain.usecase.task

import dummyAdmin
import dummyProject
import dummyTask
import io.mockk.every
import io.mockk.mockk
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.exceptions.NoLogsFoundException
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.task.GetTaskHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertTrue


class GetTaskHistoryUseCaseTest {
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private lateinit var getTaskHistoryUseCase: GetTaskHistoryUseCase

    private val project = dummyProject.copy(createdBy = dummyAdmin.id)
    private val task = dummyTask.copy(projectId = project.id)
    private val taskLogs = listOf(
        CreatedLog(
            username = "admin1",
            affectedId = task.id,
            affectedName = "T-101",
            affectedType = Log.AffectedType.TASK
        ), AddedLog(
            username = "admin1",
            affectedId = UUID.randomUUID(),
            affectedName = "M-102",
            affectedType = Log.AffectedType.MATE,
            addedTo = "task-${task.id}"
        )
    )

    @BeforeEach
    fun setup() {
        getTaskHistoryUseCase =
            GetTaskHistoryUseCase(logsRepository, projectsRepository, tasksRepository, usersRepository, Validator)
    }

    @Test
    fun `should return list of logs when task logs exist`() {
        // Given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { logsRepository.getAllLogs() } returns taskLogs
        //when
        val result = getTaskHistoryUseCase(task.id)
        //Then
        assertTrue { result.all { it.toString().contains(task.id.toString()) } }
    }

    @Test
    fun `should throw Exception when logs fetching fails `() {
        // Given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { logsRepository.getAllLogs() } throws Exception()
        // When & Then
        assertThrows<Exception> {
            getTaskHistoryUseCase(task.id)
        }
    }

    @Test
    fun `should throw NoFoundException list when no logs for the given task id`() {
        // Given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { logsRepository.getAllLogs() } returns emptyList()
        //when&//Then
        assertThrows<NoLogsFoundException> {
            getTaskHistoryUseCase(task.id)
        }
    }
}

