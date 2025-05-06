package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.FailedToAddLogException
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.UsersRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteMateFromTaskUseCaseTest {

    lateinit var tasksRepository: TasksRepository
    lateinit var deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase
    lateinit var logsRepository: LogsRepository
    lateinit var usersRepository: UsersRepository
    val task = Task(
        title = "machine learning task",
        state = "in-progress",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    )

    val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        hashedPassword = "123",
        role = UserRole.ADMIN
    )

    val mateUser = User(
        id = UUID.randomUUID(),
        username = "mate",
        hashedPassword = "5466",
        role = UserRole.MATE
    )

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        usersRepository = mockk()
        deleteMateFromTaskUseCase = DeleteMateFromTaskUseCase(tasksRepository, usersRepository, logsRepository)
    }
    @Test
    fun `should throw UnauthorizedException when user is not logged in`() {
        // Given
        val task = Task(
            id = UUID.randomUUID(),
            title = "Sample Task",
            state = "todo",
            assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()), // Assigned users with UUID
            createdBy = UUID.randomUUID(), // Created by with UUID
            projectId = UUID.randomUUID() // Project ID with UUID
        )

        every { usersRepository.getCurrentUser() } returns Result.failure(UnauthorizedException(""))

        // When & Then
        assertThrows<UnauthorizedException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }
    }

    @Test
    fun `should throw AccessDeniedException when current user is not admin`() {
        //given
        every { usersRepository.getCurrentUser() } returns Result.success(mateUser)

        //when & then
        assertThrows<AccessDeniedException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }
    }

    @Test
    fun `should throw NoFoundException when task id does not exist`() {
        //given
        every { usersRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.getTaskById(task.id) } returns Result.failure(NotFoundException(""))

        //when & then
        assertThrows<NotFoundException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }

    }

    @Test
    fun `should throw NoFoundException when mate is not assigned to the task`() {
        // Given
        every { usersRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.getTaskById(task.id) } returns Result.success(task)

        // When & Then
        assertThrows<NotFoundException> {
            deleteMateFromTaskUseCase(task.id, UUID.randomUUID())
        }
    }


    @Test
    fun `should throw FailedToAddLogException when logging mate deletion fails`() {
        // Given
        every { usersRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.getTaskById(task.id) } returns Result.success(task)
        every { logsRepository.addLog(any()) } returns Result.failure(FailedToAddLogException(""))

        // When & Then
        assertThrows<FailedToAddLogException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }
    }
    @Test
    fun `should log mate deletion when admin successfully removes mate from task`() {
        // Given
        every { usersRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.getTaskById(task.id) } returns Result.success(task)

        // When
        deleteMateFromTaskUseCase(task.id, task.assignedTo[1])

        // Then
        verify { tasksRepository.updateTask(any()) }
        verify {
            logsRepository.addLog(match { log ->
                log is DeletedLog &&
                        log.affectedId == task.id &&
                        log.affectedType == Log.AffectedType.MATE &&
                        log.username == adminUser.username
            })
        }
    }

}