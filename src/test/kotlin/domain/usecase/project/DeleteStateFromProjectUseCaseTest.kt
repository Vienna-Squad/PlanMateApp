package domain.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.repository.AuthRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class DeleteStateFromProjectUseCaseTest {

    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
    private val authRepository: AuthRepository = mockk()
    private val projectsRepository: ProjectsRepository = mockk()
    private val logsRepository: LogsRepository = mockk()
    private val projectId = UUID.fromString("project123")

    @BeforeEach
    fun setUp() {
        deleteStateFromProjectUseCase = DeleteStateFromProjectUseCase(authRepository
            ,projectsRepository,logsRepository)
    }

    @Test
    fun `should throw when deletion is successful`() {
        // given
        val state = "active"
        every { projectsRepository.getProjectById(any()) } returns Result.failure(NotFoundException(""))

        // when
        val result = deleteStateFromProjectUseCase.invoke(projectId,state)

        // then
        assertThrows<NotFoundException> {
            result
        }
    }


}
