package data.datasource.remote.mongo

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bson.Document
import org.example.data.datasource.remote.mongo.LogsMongoStorage
import org.example.data.datasource.remote.mongo.manager.base.UnEditableMongoManager
import org.example.data.datasource.remote.mongo.parser.LogMongoParser
import org.example.domain.entity.log.*
import org.example.domain.entity.log.Log.AffectedType
import org.example.domain.exceptions.AdditionException
import org.example.domain.exceptions.NoLogsFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class LogsMongoStorageTest {
    private lateinit var storage: LogsMongoStorage
    private lateinit var parser: LogMongoParser
    private val manager: UnEditableMongoManager<Log> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        parser = LogMongoParser()
        storage = LogsMongoStorage(manager)
    }

    @Test
    fun `toDocument should convert AddedLog to Document correctly`() {
        // Given
        val addedLog = AddedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "T-101",
            affectedType = AffectedType.TASK,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0),
            addedTo = "projectX"
        )

        // When
        val document = parser.serialize(addedLog)

        // Then
        assertThat(document.getString("username")).isEqualTo("testUser")
        assertThat(document.getString("affectedId")).isEqualTo(addedLog.affectedId.toString())
        assertThat(document.getString("affectedType")).isEqualTo("TASK")
        assertThat(document.getString("dateTime")).isEqualTo("2023-01-01T12:00")
        assertThat(document.getString("actionType")).isEqualTo("ADDED")
        assertThat(document.getString("addedTo")).isEqualTo("projectX")
    }

    @Test
    fun `toDocument should convert ChangedLog to Document correctly`() {
        // Given
        val changedLog = ChangedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "T-101",
            affectedType = AffectedType.TASK,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0),
            changedFrom = "TODO",
            changedTo = "IN_PROGRESS"
        )

        // When
        val document = parser.serialize(changedLog)

        // Then
        assertThat(document.getString("actionType")).isEqualTo("CHANGED")
        assertThat(document.getString("changedFrom")).isEqualTo("TODO")
        assertThat(document.getString("changedTo")).isEqualTo("IN_PROGRESS")
    }

    @Test
    fun `toDocument should convert CreatedLog to Document correctly`() {
        // Given
        val createdLog = CreatedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "P-101",
            affectedType = AffectedType.PROJECT,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0)
        )

        // When
        val document = parser.serialize(createdLog)

        // Then
        assertThat(document.getString("actionType")).isEqualTo("CREATED")
    }

    @Test
    fun `toDocument should convert DeletedLog to Document correctly`() {
        // Given
        val deletedLog = DeletedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "M-101",
            affectedType = AffectedType.MATE,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0),
            deletedFrom = "system"
        )

        // When
        val document = parser.serialize(deletedLog)

        // Then
        assertThat(document.getString("actionType")).isEqualTo("DELETED")
        assertThat(document.getString("deletedFrom")).isEqualTo("system")
    }

    @Test
    fun `fromDocument should convert Document to AddedLog correctly`() {
        // Given
        val document = Document()
            .append("username", "testUser")
            .append("affectedId", "8722f308-76cb-4a0f-8dfb-c862b28390ed")
            .append("affectedName", "P-101")
            .append("affectedType", "TASK")
            .append("dateTime", "2023-01-01T12:00")
            .append("actionType", "ADDED")
            .append("addedTo", "projectX")

        // When
        val log = parser.deserialize(document)

        // Then
        assertThat(log).isInstanceOf(AddedLog::class.java)
        val addedLog = log as AddedLog
        assertThat(addedLog.username).isEqualTo("testUser")
        assertThat(addedLog.affectedId).isEqualTo(UUID.fromString("8722f308-76cb-4a0f-8dfb-c862b28390ed"))
        assertThat(addedLog.affectedType).isEqualTo(AffectedType.TASK)
        assertThat(addedLog.dateTime).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0))
        assertThat(addedLog.addedTo).isEqualTo("projectX")
    }

    @Test
    fun `fromDocument should convert Document to ChangedLog correctly`() {
        // Given
        val document = Document()
            .append("username", "testUser")
            .append("affectedId", "8722f308-76cb-4a0f-8dfb-c862b28390ed")
            .append("affectedName", "T-101")
            .append("affectedType", "TASK")
            .append("dateTime", "2023-01-01T12:00")
            .append("actionType", "CHANGED")
            .append("changedFrom", "TODO")
            .append("changedTo", "IN_PROGRESS")

        // When
        val log = parser.deserialize(document)

        // Then
        assertThat(log).isInstanceOf(ChangedLog::class.java)
        val changedLog = log as ChangedLog
        assertThat(changedLog.changedFrom).isEqualTo("TODO")
        assertThat(changedLog.changedTo).isEqualTo("IN_PROGRESS")
    }


    @Test
    fun `getAll should return logs from collection`() {
        // Given
        val createdLog = CreatedLog(
            username = "user1",
            affectedId = UUID.randomUUID(),
            affectedName = "T-101",
            affectedType = AffectedType.TASK,
            dateTime = LocalDateTime.parse("2023-01-01T12:00")
        )
        val deletedLog = DeletedLog(
            username = "user2",
            affectedId = UUID.randomUUID(),
            affectedName = "P-101",
            affectedType = AffectedType.PROJECT,
            dateTime = LocalDateTime.parse("2023-01-02T12:00"),
            deletedFrom = "system"
        )
        every { manager.readAll() } returns listOf(createdLog, deletedLog)
        // When
        val result = storage.getAllItems()
        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isInstanceOf(CreatedLog::class.java)
        assertThat(result[1]).isInstanceOf(DeletedLog::class.java)
    }

    @Test
    fun `getAll should throw NotFoundException when no logs found`() {
        // Given
        //every { mockCollection.find() } returns mockFindIterable
        every { manager.readAll() } returns emptyList()

        // When/Then
        assertThrows<NoLogsFoundException> { storage.getAllItems() }
    }

    @Test
    fun `add should insert document into collection`() {
        // Given
        val log = CreatedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "P-101",
            affectedType = AffectedType.PROJECT,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0)
        )
        every { manager.append(log) } returns true
        // When
        storage.addItem(log)
        // Then
        verify { manager.append(match { it is CreatedLog }) }
    }

    @Test
    fun `add should throw UnknownException when insertion not acknowledged`() {
        // Given
        val log = CreatedLog(
            username = "testUser",
            affectedId = UUID.randomUUID(),
            affectedName = "",
            affectedType = AffectedType.PROJECT,
            dateTime = LocalDateTime.of(2023, 1, 1, 12, 0)
        )
        every { manager.append(log) } returns false
        // When/Then
        assertThrows<AdditionException> { storage.addItem(log) }
    }
}