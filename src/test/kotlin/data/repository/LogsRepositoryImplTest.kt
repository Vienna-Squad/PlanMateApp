package data.repository

import com.google.common.truth.Truth.assertThat
import data.datasource.DataSource
import dummyLogs
import io.mockk.*
import org.example.data.repository.LogsRepositoryImpl
import org.example.domain.PlanMateAppException
import org.example.domain.entity.log.Log
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class LogsRepositoryImplTest {
    private lateinit var logsRepository: LogsRepositoryImpl
    private val logsDataSource: DataSource<Log> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        logsRepository = LogsRepositoryImpl(logsDataSource)
    }

    @Test
    fun `should return all logs when logs are existed`() {
        //given
        every { logsDataSource.getAll() } returns dummyLogs
        //when
        val result = logsRepository.getAllLogs()
        //then
        assertThat(result.size).isEqualTo(dummyLogs.size)
        verify { logsDataSource.getAll() }
    }

    @Test
    fun `should add logs when pass a valid log`() {
        //given
        every { logsDataSource.add(dummyLogs[2]) } just Runs
        //when
        logsRepository.addLog(dummyLogs[2])
        //then
        verify { logsDataSource.add(match { it == dummyLogs[2] }) }
    }

    @Test
    fun `should throw PlanMateAppException when data source throw any exception while retrieval`() {
        //given
        every { logsDataSource.getAll() } throws Exception()
        //when && then
        assertThrows<PlanMateAppException> { logsRepository.getAllLogs() }
    }

    @Test
    fun `should throw PlanMateAppException when data source throw any exception while adding`() {
        //given
        every { logsDataSource.add(dummyLogs[2]) } throws Exception()
        //when && then
        assertThrows<PlanMateAppException> { logsRepository.addLog(dummyLogs[2]) }
    }
}