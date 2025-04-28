package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.entity.Project
import org.example.domain.repository.ProjectsRepository
import org.example.domain.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {


    lateinit var prjectRepository: ProjectsRepository
    lateinit var createProjectUseCase: CreateProjectUseCase

    val name = "graduation project"
    val states = listOf("done", "in-progress", "todo")
    val createdBy = "20"
    val matesIds = listOf("1", "2", "3", "4", "5")
    val newProject = Project(name, states, createdBy, matesIds)

    @BeforeEach
    fun setUp() {

        prjectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(prjectRepository)

    }


    @Test
    fun `should create project and save it when valid data is provided`() {

        //given

        every { prjectRepository.addProject(newProject) } returns Unit

        // when
        createProjectUseCase(name, states, createdBy, matesIds)

        // then
        verify(exactly = 1) { prjectRepository.addProject(newProject) }

    }

    @Test
    fun `should throw exception when project not added`() {
        //given
        every { prjectRepository.addProject(any()) } throws Exception("failed to add project")

        //when & then
        assertThrows<Exception> {
            createProjectUseCase(name, states, createdBy, matesIds)
        }


    }


}