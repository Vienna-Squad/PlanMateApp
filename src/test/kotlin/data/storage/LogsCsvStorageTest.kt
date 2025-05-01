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
import java.text.ParseException

class LogsCsvStorageTest {
    private lateinit var tempFile: File
    private lateinit var storage: LogsCsvStorage

    private val dummyLogs = listOf(
        CreatedLog("admin1", "P-001", Log.AffectedType.PROJECT),
        AddedLog("admin1", "M-001", Log.AffectedType.MATE, addedTo = "P-001"),
        AddedLog("admin2", "S-001", Log.AffectedType.STATE, addedTo = "P-001"),
        DeletedLog("admin2", "M-001", Log.AffectedType.MATE, deletedFrom = "P-001"),
        ChangedLog("mate1", "T-002", Log.AffectedType.TASK, changedFrom = "TODO", changedTo = "InProgress"),
        ChangedLog("admin1", "S-002", Log.AffectedType.STATE, changedFrom = "New", changedTo = "ToDo"),
        CreatedLog("admin3", "T-004", Log.AffectedType.TASK),
        DeletedLog("admin3", "M-001", Log.AffectedType.MATE, deletedFrom = "project P-001"),
    )

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFile = tempDir.resolve("logs_test.csv").toFile()
        storage = LogsCsvStorage(tempFile)
    }

    @Test
    fun `should append & read logs correctly when file is exist`() {
        dummyLogs.forEach { storage.append(it) }
        val logs = storage.read()
        assertThat(logs.size).isEqualTo(8)
    }

    @Test
    fun `should throw FileNotFoundException when try to read from not exist file`() {
        storage = LogsCsvStorage(File("not_exist_file.csv"))
        assertThrows<FileNotFoundException> { storage.read() }
    }

    @Test
    fun `should create file if not exists and append log correctly`() {
        val newFile = File("new_file.csv")
        storage = LogsCsvStorage(newFile)
        assertThrows<FileNotFoundException> { storage.read() }
        storage.append(dummyLogs.first())
        val logs = storage.read()
        assertThat(logs[0].toString()).isEqualTo(dummyLogs[0].toString())
        newFile.deleteOnExit()
    }

    @Test
    fun `should throw ParseException when parse wrong ActionType while reading`() {
        tempFile.writeText("MOHANNAD,admin2,M-001,MATE,2025-04-29T11:50:10.828790400,,\n")
        assertThrows<ParseException> { storage.read() }
    }

    @Test
    fun `should throw ParseException when parse line with wrong number of fields while reading`() {
        tempFile.writeText("CREATED,P-001,2025-04-29T11:50:10.811710400,,\n")
        assertThrows<ParseException> { storage.read() }
    }
}