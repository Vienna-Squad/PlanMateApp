package domain.usecase.project

import dummyAdmin
import dummyMate
import dummyMateId
import dummyProject
import dummyProjectId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.MateAlreadyExists
import org.example.domain.ProjectAccessDenied
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.repository.UsersRepository
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddMateToProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase


    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        usersRepository = mockk(relaxed = true)
        addMateToProjectUseCase = AddMateToProjectUseCase(projectsRepository, logsRepository, usersRepository)
    }


    @Test
    fun `should throw AccessDeniedException when who creates project is not current user`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyMate
        every { projectsRepository.getProjectById(any()) } returns dummyProject
        // when & then
        assertThrows<ProjectAccessDenied> {
            addMateToProjectUseCase.invoke(projectId = dummyProjectId, mateId = dummyMateId)
        }
    }

    @Test
    fun `should throw AlreadyExistException when mate already found in project`() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(
            id = dummyProjectId,
            createdBy = dummyAdmin.id,
            matesIds = listOf(dummyMateId)
        )
        every { usersRepository.getUserByID(any()) } returns dummyMate.copy(id = dummyMateId)
        // when & then
        assertThrows<MateAlreadyExists> {
            addMateToProjectUseCase.invoke(projectId = dummyProjectId, mateId = dummyMateId)
        }
    }

    @Test
    fun `should complete addition of mate to project `() {
        // given
        every { usersRepository.getCurrentUser() } returns dummyAdmin
        every { projectsRepository.getProjectById(any()) } returns dummyProject.copy(
            id = dummyProjectId,
            createdBy = dummyAdmin.id,
            matesIds = listOf()
        )
        every { usersRepository.getUserByID(any()) } returns dummyMate.copy(id = dummyMateId)
        // when
        addMateToProjectUseCase.invoke(projectId = dummyProjectId, mateId = dummyMateId)
        // then
        verify { projectsRepository.updateProject(any()) }
        verify { logsRepository.addLog(any()) }
    }


}