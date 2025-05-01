package data.storage.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.storage.LogsCsvStorage
import org.example.data.storage.repository.LogsCsvRepository
import org.example.domain.NoFoundException
import org.example.domain.entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.Test

class LogsCsvRepositoryTest{

        private lateinit var repository: LogsCsvRepository
        private lateinit var storage: LogsCsvStorage

        private val createdLog = CreatedLog(
            username = "user1",
            affectedId = "P1",
            affectedType = Log.AffectedType.PROJECT,
            dateTime = LocalDateTime.now()
        )

        private val addedLog = AddedLog(
            username = "user1",
            affectedId = "T1",
            affectedType = Log.AffectedType.TASK,
            dateTime = LocalDateTime.now(),
            addedTo = "P1"
        )

        private val changedLog = ChangedLog(
            username = "user1",
            affectedId = "T1",
            affectedType = Log.AffectedType.TASK,
            dateTime = LocalDateTime.now(),
            changedFrom = "ToDo",
            changedTo = "Done"
        )

        private val deletedLog = DeletedLog(
            username = "user1",
            affectedId = "T1",
            affectedType = Log.AffectedType.TASK,
            dateTime = LocalDateTime.now(),
            deletedFrom = "P1"
        )

        @BeforeEach
        fun setup() {
            storage = mockk(relaxed = true)
            repository = LogsCsvRepository(storage)
        }

        @Test
        fun `should return list of logs when getAll is called`() {
            // Given
            every { storage.read() } returns listOf(createdLog, addedLog, changedLog, deletedLog)

            // When
            val result = repository.getAll()

            // Then
            assertEquals(listOf(createdLog, addedLog, changedLog, deletedLog), result.getOrThrow())
        }

        @Test
        fun `should return failure when getAll fails to read`() {
            // Given
            every { storage.read() } throws NoFoundException()

            // When
            val result = repository.getAll()

            // Then
            assertTrue(result.isFailure)
        }

        @Test
        fun `should add log successfully when add is called`() {
            // Given
            every { storage.read() } returns listOf(createdLog)

            // When
            val result = repository.add(addedLog)

            // Then
            verify { storage.append(addedLog) }
        }

        @Test
        fun `should return failure when add fails`() {
            // Given
            every { storage.append(addedLog) } throws NoFoundException()

            // When
            val result = repository.add(addedLog)

            // Then
            assertTrue(result.isFailure)
        }
    }
