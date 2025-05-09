package domain.usecase.project

import io.mockk.mockk
import io.mockk.verify
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddStateToProjectUseCaseTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var logsRepository: LogsRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    private val projectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val state = "done.."

    @BeforeEach
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        logsRepository = mockk(relaxed = true)
        addStateToProjectUseCase =
            AddStateToProjectUseCase(projectsRepository, logsRepository,mockk(relaxed = true))

    }

    @Test
    fun `should call updated project`() {
        // when
        addStateToProjectUseCase(projectId = projectId ,   state)
        // then
        verify { projectsRepository.getProjectById(any()) }
    }


    @Test
    fun `should add log `() {
        // when
        addStateToProjectUseCase(projectId = projectId ,state)
        // then
        verify { logsRepository.addLog(any()) }
    }

}



