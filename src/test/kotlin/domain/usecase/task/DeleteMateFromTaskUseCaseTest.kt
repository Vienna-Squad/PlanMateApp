package domain.usecase.task

import dummyAdmin
import dummyMate
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.MateNotAssignedToTaskException
import org.example.domain.TaskAccessDeniedException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.Validator
import org.example.domain.usecase.task.DeleteMateFromTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteMateFromTaskUseCaseTest {


    lateinit var deleteMateFromTaskUseCase: DeleteMateFromTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        deleteMateFromTaskUseCase =
            DeleteMateFromTaskUseCase(
                tasksRepository,
                logsRepository,
                usersRepository,
                projectsRepository,
                Validator
            )
    }

    @Test
    fun `should delete mate when given task id and mate id`() {
        //Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(createdBy = dummyAdmin.id, projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        // When
        deleteMateFromTaskUseCase(task.id, task.assignedTo[0])
        //Then
        verify {
            tasksRepository.updateTask(
                match {
                    !
                    (it.assignedTo.contains(task.assignedTo[0]))
                })
        }
        verify { logsRepository.addLog(match { it is DeletedLog }) }
    }

    @Test
    fun `should throw TaskAccessDeniedException project not created by current user`() {
        //Given
        val project = dummyProject
        val task = dummyTask.copy(createdBy = dummyAdmin.id, projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        // When&then
        assertThrows<TaskAccessDeniedException> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[0])
        }
    }

    @Test
    fun `should throw TaskHasNoException when current user not assigned to task given task id & mate id`() {
        //Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(createdBy = dummyAdmin.id, projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        // When&then
        assertThrows<MateNotAssignedToTaskException> {
            deleteMateFromTaskUseCase(task.id, UUID.randomUUID())
        }
    }


    @Test
    fun `should not complete execution when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { deleteMateFromTaskUseCase(dummyTask.id, dummyMate.id) }
        verify(exactly = 0) { tasksRepository.getTaskById(any()) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getTaskById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteMateFromTaskUseCase(dummyTask.id, dummyAdmin.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns dummyTask
        every { projectsRepository.getProjectById(dummyTask.projectId) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteMateFromTaskUseCase(dummyTask.id, dummyAdmin.id) }
    }

    @Test
    fun `should throw Exception when tasksRepository updateTask throw Exception given task id`() {
        //Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(createdBy = dummyAdmin.id, projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.updateTask(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[0])
        }
        verify(exactly = 0) { logsRepository.addLog(match { it is DeletedLog }) }

    }

    @Test
    fun `should throw Exception when addLog fails `() {
        //Given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTask.copy(createdBy = dummyAdmin.id, projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(dummyTask.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.updateTask(any()) } returns Unit
        every { logsRepository.addLog(any()) } throws Exception()
        // When & Then
        assertThrows<Exception> {
            deleteMateFromTaskUseCase(task.id, task.assignedTo[0])
        }

    }
}

private val dummyTask = dummyTasks[0]