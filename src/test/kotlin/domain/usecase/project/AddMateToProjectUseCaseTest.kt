package domain.usecase.project

import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddMateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddMateToProjectUseCaseTest {
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var addMateToProjectUseCase: AddMateToProjectUseCase

    private val projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val mateId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")

    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        addMateToProjectUseCase = AddMateToProjectUseCase(projectsRepository, logsRepository,mockk(relaxed = true))
    }


    @Test
    fun `should call updated project`() {
        // when
        addMateToProjectUseCase.invoke(projectId = projectId , mateId = mateId )
        // then
        verify { projectsRepository.getProjectById(any()) }
    }

    @Test
    fun `should call getProjectById`() {
        // when
        addMateToProjectUseCase.invoke(projectId = projectId , mateId = mateId )
        // then
        verify { projectsRepository.updateProject(any()) }
    }

    @Test
    fun `should add log `() {
        // when
        addMateToProjectUseCase.invoke(projectId = projectId , mateId = mateId )
        // then
        verify { logsRepository.addLog(any()) }
    }




}