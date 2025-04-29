package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.FailedToAddProjectException
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {


    lateinit var projectRepository: ProjectsRepository
    lateinit var createProjectUseCase: CreateProjectUseCase

    val name = "graduation project"
    val states = listOf("done", "in-progress", "todo")
    val createdBy = "20"
    val matesIds = listOf("1", "2", "3", "4", "5")
    val newProject = Project(name, states, createdBy, matesIds)

    @BeforeEach
    fun setUp() {

        projectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository)

    }


    @Test
    fun `should create project and save it when valid data is provided`() {
        //given
        every { projectRepository.addProject(newProject) } returns Unit

        // when
        createProjectUseCase(name, states, createdBy, matesIds)

        // then
        verify(exactly = 1) { projectRepository.addProject(newProject) }

    }

    @Test
    fun `should throw FailedToAddProjectException when project not added`() {
        //given
        every { projectRepository.addProject(any()) } throws FailedToAddProjectException()

        //when & then
        assertThrows<FailedToAddProjectException> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }


    }


}