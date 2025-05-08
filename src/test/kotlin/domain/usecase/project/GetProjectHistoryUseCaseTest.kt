package domain.usecase.project

import com.google.common.truth.Truth.assertThat
import dummyLogs
import dummyProject
import io.mockk.every
import io.mockk.mockk
import org.example.domain.NotFoundException
import org.example.domain.entity.AddedLog
import org.example.domain.entity.CreatedLog
import org.example.domain.entity.Log
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
                affectedId = dummyProject.id.toString(),
                affectedType = Log.AffectedType.PROJECT
            ), AddedLog(
                username = "admin1",
                affectedId = UUID.randomUUID().toString(),
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
            it.affectedId == dummyProject.id.toString() || it.toString().contains(dummyProject.id.toString())
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
