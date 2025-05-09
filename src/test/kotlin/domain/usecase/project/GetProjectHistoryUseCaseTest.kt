package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyLogs
import dummyProject
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.Log
import org.example.domain.repository.LogsRepository
import org.example.domain.usecase.project.GetProjectHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetProjectHistoryUseCaseTest {

    private lateinit var getProjectHistoryUseCase: GetProjectHistoryUseCase
    private val logsRepository: LogsRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getProjectHistoryUseCase = GetProjectHistoryUseCase(logsRepository)
    }

    @Test
    fun `should retrieve all logs of project when it contains the project id`() {
        //given
        val projectLogs = listOf(
            CreatedLog(
                username = "admin1",
                affectedId = dummyProject.id,
                affectedName = "P-101",
                affectedType = Log.AffectedType.PROJECT
            ), AddedLog(
                username = "admin1",
                affectedId = UUID.randomUUID(),
                affectedName = "P-102",
                affectedType = Log.AffectedType.STATE,
                addedTo = "project-${dummyProject.id}"
            )
        )
        every { logsRepository.getAllLogs() } returns dummyLogs + projectLogs
        //when
        val result = getProjectHistoryUseCase(dummyProject.id)
        //then
        assertThat(result.size).isEqualTo(2)
        assertThat(result.all {
            it.affectedId == dummyProject.id || it.toString().contains(dummyProject.id.toString())
        }).isTrue()
    }

    @Test
    fun `should throw NotFoundException when no log contains the project id`() {
        //given
        every { logsRepository.getAllLogs() } returns dummyLogs
        //when && then
        assertThrows<NotFoundException> { getProjectHistoryUseCase(dummyProject.id) }
    }
}
