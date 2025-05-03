package data.storage.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.storage.LogsCsvStorage
import org.example.data.storage.repository.LogsRepositoryImpl
import org.example.domain.NotFoundException
import org.example.domain.entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class LogsRepositoryImplTest{

        private lateinit var repository: LogsRepositoryImpl
        private lateinit var storage: LogsCsvStorage

    private val createdLog = CreatedLog(
        username = "user1",
        affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        affectedType = Log.AffectedType.PROJECT,
        dateTime = LocalDateTime.now()
    )

    private val addedLog = AddedLog(
        username = "user1",
        affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        affectedType = Log.AffectedType.TASK,
        dateTime = LocalDateTime.now(),
        addedTo = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    )

    private val changedLog = ChangedLog(
        username = "user1",
        affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        affectedType = Log.AffectedType.TASK,
        dateTime = LocalDateTime.now(),
        changedFrom = "ToDo",
        changedTo = "Done"
    )

    private val deletedLog = DeletedLog(
        username = "user1",
        affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        affectedType = Log.AffectedType.TASK,
        dateTime = LocalDateTime.now(),
        deletedFrom = "P2"
    )


    @BeforeEach
        fun setup() {
            storage = mockk(relaxed = true)
            repository = LogsRepositoryImpl(storage)
        }

        @Test
        fun `should return list of logs when getAll is called`() {
            // Given
            every { storage.read() } returns listOf(createdLog, addedLog, changedLog, deletedLog)

            // When
            val result = repository.getAllLogs()

            // Then
            assertEquals(listOf(createdLog, addedLog, changedLog, deletedLog), result.getOrThrow())
        }

        @Test
        fun `should return failure when getAll fails to read`() {
            // Given
            every { storage.read() } throws NotFoundException("get all fail")

            // When
            val result = repository.getAllLogs()

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `should add log successfully when add is called`() {
            // Given
            every { storage.read() } returns listOf(createdLog)

            // When
            val result = repository.addLog(addedLog)

            // Then
            verify { storage.append(addedLog) }
        }

        @Test
        fun `should return failure when add fails`() {
            // Given
            every { storage.append(addedLog) } throws NotFoundException("add fail")

            // When
            val result = repository.addLog(addedLog)

            // Then
            assertTrue(result.isFailure)
        }
    }
