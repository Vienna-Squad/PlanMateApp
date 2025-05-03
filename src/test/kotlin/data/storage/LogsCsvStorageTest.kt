package data.storage

import com.google.common.truth.Truth.assertThat
import org.example.data.storage.LogsCsvStorage
import org.example.domain.entity.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*


class LogsCsvStorageTest {
    private lateinit var tempFile: File
    private lateinit var storage: LogsCsvStorage

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFile = tempDir.resolve("logs_test.csv").toFile()
        storage = LogsCsvStorage(tempFile)
    }

    @Test
    fun `should create file with header when initialized`() {
        // Given - initialized in setUp

        // When - file creation happens in init block

        // Then
        assertThat(tempFile.exists()).isTrue()
        assertThat(tempFile.readText()).contains("ActionType,username,affectedId,affectedType,dateTime,changedFrom,changedTo")
    }

    @Test
    fun `should correctly serialize and append ChangedLog`() {
        // Given
        val changedLog = ChangedLog(
            username = "user1",
            affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            affectedType = Log.AffectedType.TASK,
            dateTime = LocalDateTime.parse("2023-01-01T10:15:30"),
            changedFrom = "TODO",
            changedTo = "In Progress"
        )


        // When
        storage.append(changedLog)

        // Then
        val logs = storage.read()
        assertThat(logs).hasSize(1)
        assertThat(logs[0]).isInstanceOf(ChangedLog::class.java)

        val savedLog = logs[0] as ChangedLog
        assertThat(savedLog.username).isEqualTo("user1")
        assertThat(savedLog.affectedId).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
        assertThat(savedLog.changedFrom).isEqualTo("TODO")
        assertThat(savedLog.changedTo).isEqualTo("In Progress")
    }

    @Test
    fun `should correctly serialize and append AddedLog`() {
        // Given
        val addedLog = AddedLog(
            username = "user1",
            affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
            affectedType = Log.AffectedType.MATE,
            dateTime = LocalDateTime.parse("2023-01-01T10:15:30"),
            addedTo = UUID.fromString("550e8400-e29b-41d4-a716-446655440002")
        )
        // When
        storage.append(addedLog)

        // Then
        val logs = storage.read()
        assertThat(logs).hasSize(1)
        assertThat(logs[0]).isInstanceOf(AddedLog::class.java)

        val savedLog = logs[0] as AddedLog
        assertThat(savedLog.username).isEqualTo("user1")
        assertThat(savedLog.affectedId).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
        assertThat(savedLog.addedTo).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
    }

    @Test
    fun `should correctly serialize and append DeletedLog`() {
        // Given
        val deletedLog = DeletedLog(
            username = "user1",
            affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
            affectedType = Log.AffectedType.STATE,
            dateTime = LocalDateTime.parse("2023-01-01T10:15:30"),
            deletedFrom = "project456"
        )


        // When
        storage.append(deletedLog)

        // Then
        val logs = storage.read()
        assertThat(logs).hasSize(1)
        assertThat(logs[0]).isInstanceOf(DeletedLog::class.java)

        val savedLog = logs[0] as DeletedLog
        assertThat(savedLog.username).isEqualTo("user1")
        assertThat(savedLog.affectedId).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
    }

    @Test
    fun `should correctly serialize and append CreatedLog`() {
        // Given
        val createdLog = CreatedLog(
            username = "user1",
            affectedId = UUID.fromString("550e8400-e29b-41d4-a716-446655440005"),
            affectedType = Log.AffectedType.PROJECT,
            dateTime = LocalDateTime.parse("2023-01-01T10:15:30")
        )

        // When
        storage.append(createdLog)

        // Then
        val logs = storage.read()
        assertThat(logs).hasSize(1)
        assertThat(logs[0]).isInstanceOf(CreatedLog::class.java)

        val savedLog = logs[0] as CreatedLog
        assertThat(savedLog.username).isEqualTo("user1")
        assertThat(savedLog.affectedId).isEqualTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))
        assertThat(savedLog.affectedType).isEqualTo(Log.AffectedType.PROJECT)
    }

    @Test
    fun `should append multiple logs in order`() {
        // Given
        val log1 = CreatedLog("user1", UUID.fromString("550e8400-e29b-41d4-a716-446655440006"), Log.AffectedType.PROJECT,
            LocalDateTime.parse("2023-01-01T10:00:00"))
        val log2 = AddedLog("user1", UUID.fromString("550e8400-e29b-41d4-a716-446655440007"), Log.AffectedType.MATE,
            LocalDateTime.parse("2023-01-01T10:15:00"), UUID.fromString("550e8400-e29b-41d4-a716-446655440008"))
        val log3 = ChangedLog("user2", UUID.fromString("550e8400-e29b-41d4-a716-446655440009"), Log.AffectedType.TASK,
            LocalDateTime.parse("2023-01-01T11:00:00"), "TODO", "In Progress")

        // When
        storage.append(log1)
        storage.append(log2)
        storage.append(log3)

        // Then
        val logs = storage.read()
        assertThat(logs).hasSize(3)
        assertThat(logs[0]).isInstanceOf(CreatedLog::class.java)
        assertThat(logs[1]).isInstanceOf(AddedLog::class.java)
        assertThat(logs[2]).isInstanceOf(ChangedLog::class.java)
    }

    @Test
    fun `should handle reading from non-existent file`() {
        // Given
        val nonExistentFile = File("non_existent_file.csv")
        val invalidStorage = LogsCsvStorage(nonExistentFile)

        // Ensure the file doesn't exist before reading
        if (nonExistentFile.exists()) {
            nonExistentFile.delete()
        }

        // When/Then
        assertThrows<FileNotFoundException> { invalidStorage.read() }
    }

    @Test
    fun `should throw IllegalArgumentException when reading malformed CSV`() {
        // Given
        tempFile.writeText("INVALID_ACTION,user1,id123,TASK,2023-01-01T10:00:00,,\n")

        // When/Then
        assertThrows<IllegalArgumentException> { storage.read() }
    }

    @Test
    fun `should throw IllegalArgumentException when CSV has wrong number of columns`() {
        // Given
        tempFile.writeText("CREATED,user1,id123\n")

        // When/Then
        assertThrows<IllegalArgumentException> { storage.read() }
    }

    @Test
    fun `should return empty list when file has only header`() {
        // Given
        // Only header is written during initialization

        // When
        val logs = storage.read()

        // Then
        assertThat(logs).isEmpty()
    }
}