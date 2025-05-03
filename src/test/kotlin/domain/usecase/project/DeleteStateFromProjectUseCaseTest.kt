package domain.usecase.project

import domain.usecase.project.DeleteStateFromProjectUseCase
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.repository.AuthenticationRepository
import org.example.domain.repository.LogsRepository
import org.example.domain.repository.ProjectsRepository
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.mp.KoinPlatform.getKoin
import java.util.UUID

class DeleteStateFromProjectUseCaseTest {

    private lateinit var deleteStateFromProjectUseCase: DeleteStateFromProjectUseCase
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val projectsRepository: ProjectsRepository = mockk()
    private val logsRepository: LogsRepository = mockk()
    private val projectId = UUID.fromString("project123")

    @BeforeEach
    fun setUp() {
        deleteStateFromProjectUseCase = DeleteStateFromProjectUseCase(authenticationRepository
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
