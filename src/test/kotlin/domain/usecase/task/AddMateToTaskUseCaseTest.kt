package domain.usecase.task

import dummyAdmin
import dummyMate
import dummyProject
import dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.ProjectHasNoException
import org.example.domain.entity.log.AddedLog
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.AddMateToTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddMateToTaskUseCaseTest {

    private lateinit var addMateToTaskUseCase: AddMateToTaskUseCase
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        addMateToTaskUseCase = AddMateToTaskUseCase(
            tasksRepository,
            logsRepository,
            usersRepository,
            projectsRepository,
        )
    }

    @Test
    fun `should add mate to task when project creator add mate to task`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id)
        //then
        verify { tasksRepository.updateTask(match { dummyMate.id in it.assignedTo }) }
        verify { logsRepository.addLog(match { it is AddedLog }) }
    }

    @Test
    fun `should add mate to task when project mate add another mate to task`() {
        //given
        val anotherMate = dummyProject.matesIds.random()
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when
        addMateToTaskUseCase(taskId = task.id, mateId = anotherMate)
        //then
        verify { tasksRepository.updateTask(match { anotherMate in it.assignedTo }) }
        verify { logsRepository.addLog(match { it is AddedLog }) }
    }

    @Test
    fun `should throw AccessDeniedException when non-project-related admin add mate to task`() {
        //given
        val project = dummyProject.copy(matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<AccessDeniedException> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw AccessDeniedException when non-project-related mate add mate to task`() {
        //given
        val task = dummyTasks.random().copy(projectId = dummyProject.id)
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(dummyProject.id) } returns dummyProject
        //when && then
        assertThrows<AccessDeniedException> {
            addMateToTaskUseCase(
                taskId = task.id,
                mateId = dummyProject.matesIds.random()
            )
        }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }


    @Test
    fun `should throw AlreadyExistException when user add already assigned mate`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = dummyProject.id, assignedTo = listOf(dummyMate.id))
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<AlreadyExistException> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }

    @Test
    fun `should throw ProjectHasNoException when user add non-project-related mate`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id)
        val task = dummyTasks.random().copy(projectId = dummyProject.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        //when && then
        assertThrows<ProjectHasNoException> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
    }


    @Test
    fun `should not proceed when getCurrentUser fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { tasksRepository.getTaskById(any()) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
    }

    @Test
    fun `should not proceed when getTaskById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { projectsRepository.getProjectById(any()) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
    }

    @Test
    fun `should not proceed when getProjectById fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { tasksRepository.updateTask(any()) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
    }

    @Test
    fun `should not proceed when updateTask fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        every { tasksRepository.updateTask(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
        verify(exactly = 0) { logsRepository.addLog(any()) }
        verify(exactly = 0) { usersRepository.getUserByID(any()) }
    }

    @Test
    fun `should not proceed when addLog fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        every { logsRepository.addLog(any()) } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
    }

    @Test
    fun `should not proceed when getUserByID fails`() {
        //given
        val project = dummyProject.copy(createdBy = dummyAdmin.id, matesIds = dummyProject.matesIds + dummyMate.id)
        val task = dummyTasks.random().copy(projectId = project.id)
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { tasksRepository.getTaskById(task.id) } returns task
        every { projectsRepository.getProjectById(project.id) } returns project
        every { usersRepository.getUserByID(dummyMate.id) } throws Exception()
        //when && then
        assertThrows<Exception> { addMateToTaskUseCase(taskId = task.id, mateId = dummyMate.id) }
    }


}
