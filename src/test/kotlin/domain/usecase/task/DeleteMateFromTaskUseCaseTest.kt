package domain.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.FailedToDeleteMate
import org.example.domain.NoMateFoundException
import org.example.domain.NoTaskFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.*
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.usecase.project.CreateProjectUseCase
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class DeleteMateFromTaskUseCaseTest {

    lateinit var tasksRepository: TasksRepository
    lateinit var deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase
    lateinit var logsRepository: LogsRepository
    lateinit var authRepository: AuthenticationRepository

    val task = Task(
        title = "machine learning task",
        state = "in-progress",
        assignedTo = listOf("nada", "hend", "mariam"),
        createdBy = "admin1",
        projectId = ""
    )
    val updatedTask=Task(
        title = "machine learning task",
        state = "in-progress",
        assignedTo = listOf("nada", "mariam"),
        createdBy = "admin1",
        projectId = ""
    )
    val adminUser = User(username = "admin", "123", type = UserType.ADMIN)
    val mateUser = User(username = "mate", "5466", type = UserType.MATE)

    @BeforeEach
    fun setUp() {
        tasksRepository = mockk()
        logsRepository = mockk()
        authRepository = mockk()
        deleteMateFromTaskUseCase = DeleteMateFromTaskUseCase(tasksRepository, authRepository, logsRepository)
    }

    @Test
    fun `should throw UnauthorizedException when user is not logged in`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.failure(UnauthorizedException())

        //when & then
        assertThrows<UnauthorizedException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }
    }

    @Test
    fun `should throw UnauthorizedException when current user is not admin`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(mateUser)

        //when & then
        assertThrows<UnauthorizedException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }
    }

    @Test
    fun `should throw NoTaskFoundException when task id does not exist`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(task.id) } returns Result.failure(NoTaskFoundException(""))

        //when & then
        assertThrows<NoTaskFoundException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])
        }

    }

    @Test
    fun `should return task when task id exists`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(task.id) } returns Result.success(task)
        every { logsRepository.add(any()) } returns Result.success(Unit)
        every { tasksRepository.update(any()) } returns Result.success(Unit)
        //when
        deleteMateFromTaskUseCase(task.id, task.assignedTo[1])

        //then
        verify {
            tasksRepository.get(task.id)
            tasksRepository.update(any())
            logsRepository.add(any())
        }

    }

    @Test
    fun `should throw NoMateFoundException when mate is not assigned to the task`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(task.id) } returns Result.success(task)

        //when & then
        assertThrows<NoMateFoundException> {
            deleteMateFromTaskUseCase(task.id, "no-mate-found")
        }

    }


    @Test
    fun `should throw FailedToDeleteMate when logging mate deletion fails`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(task.id) } returns Result.success(task)
        every { tasksRepository.update(any()) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.failure(FailedToDeleteMate())



        //when & then
        assertThrows<FailedToDeleteMate>{
            deleteMateFromTaskUseCase(task.id, task.assignedTo[1])

        }
    }

    @Test
    fun `should create log mate deletion when admin removes mate from task successfully`() {
        //given
        every { authRepository.getCurrentUser() } returns Result.success(adminUser)
        every { tasksRepository.get(task.id) } returns Result.success(task)
        every { tasksRepository.update(updatedTask) } returns Result.success(Unit)
        every { logsRepository.add(any()) } returns Result.success(Unit)

        // when
        deleteMateFromTaskUseCase(task.id, task.assignedTo[1])

        // then
        verify {
            logsRepository.add(match {
                it is CreatedLog
            })
        }
        verify { tasksRepository.update(updatedTask) }
    }

}