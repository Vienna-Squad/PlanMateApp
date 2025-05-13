package domain.usecase.task

import dummyMate
import dummyProject
import dummyTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.NoChangeExceptionException
import org.example.domain.ProjectAccessDeniedException
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.TasksRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.task.EditTaskTitleUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class EditTaskTitleUseCaseTest {

    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val logsRepository: LogsRepository = mockk(relaxed = true)
    private val usersRepository: UsersRepository = mockk(relaxed = true)
    private val projectsRepository: ProjectsRepository = mockk(relaxed = true)
    lateinit var editTaskTitleUseCase: EditTaskTitleUseCase

    @BeforeEach
    fun setUp() {
        editTaskTitleUseCase =
            EditTaskTitleUseCase(tasksRepository, logsRepository, usersRepository, projectsRepository)
    }

    @Test
    fun `should throw ProjectAccessDenied when current user not create project`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject
        // when & then
        assertThrows<ProjectAccessDeniedException> {
            editTaskTitleUseCase.invoke(taskId = dummyTask.id, newTitle = "School Library")
        }
    }

    @Test
    fun `should throw ProjectAccessDenied when current user not from team in project`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(createdBy = dummyMate.id, matesIds = listOf())
        // when & then
        assertThrows<ProjectAccessDeniedException> {
            editTaskTitleUseCase.invoke(taskId = dummyTask.id, newTitle = "School Library")
        }
    }


    @Test
    fun `should throw NoChangeException when new title is the same of old title`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(createdBy = dummyMate.id , matesIds = listOf(dummyMate.id))
        every { tasksRepository.getTaskById(any()) } returns dummyTask.copy(title = "School Library")
        // when & then
        assertThrows<NoChangeExceptionException> {
            editTaskTitleUseCase.invoke(taskId = dummyTask.id, newTitle = "School Library")
        }
    }



    @Test
    fun `invoke should edit task when the task id is valid`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(createdBy = dummyMate.id , matesIds = listOf(dummyMate.id))
        every { tasksRepository.getTaskById(any()) } returns dummyTask.copy(id = dummyTask.id,title = "i hate final exams")

        // when
        editTaskTitleUseCase.invoke(taskId = dummyTask.id, newTitle = "School Library")

        // then
        verify { tasksRepository.updateTask(any()) }
        verify { logsRepository.addLog(any()) }
    }


}