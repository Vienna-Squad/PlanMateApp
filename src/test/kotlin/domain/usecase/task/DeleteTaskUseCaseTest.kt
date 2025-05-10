package domain.usecase.task

import dummyAdmin
import dummyProject
import dummyTasks
import io.mockk.*
import org.example.domain.AccessDeniedException
import org.example.domain.entity.log.DeletedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    val dummyTask = dummyTasks.random()

    @BeforeEach
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(
            tasksRepository,
            logsRepository,
            usersRepository,
            projectsRepository
        )
    }

    @Test
    fun `should delete task and log given user is creator`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        //when
        deleteTaskUseCase(task.id)
        //then
        verify { tasksRepository.deleteTaskById(match { it == task.id }) }
        verify { logsRepository.addLog(match { it is DeletedLog}) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not the task creator`() {
        //given
        val task = dummyTasks.random().copy(projectId = dummyProject.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<AccessDeniedException> { deleteTaskUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.deleteProjectById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getCurrentUser fails`() {
        //given
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { deleteTaskUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.deleteTaskById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when getProjectById fails`() {
        //given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteTaskUseCase(dummyProject.id) }
        verify(exactly = 0) { projectsRepository.deleteProjectById(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when deleteTask fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { tasksRepository.deleteTaskById(task.id) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteTaskUseCase(task.id) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should not complete execution when addLog fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(task.projectId) } returns project
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { deleteTaskUseCase(task.id) }
    }

    @Test
    fun `should not log if task deletion fails`() {
        // Given
        every { tasksRepository.deleteTaskById(dummyTask.id) } throws Exception()

        // Then& When
        assertThrows<Exception> {
            tasksRepository.deleteTaskById(
                dummyTask.id
            )
        }
        verify(exactly = 0) {
            logsRepository.addLog(
                match {
                    it is DeletedLog &&
                            it.affectedId == dummyTask.id &&
                            it.affectedType == Log.AffectedType.TASK


                })
        }
    }

}