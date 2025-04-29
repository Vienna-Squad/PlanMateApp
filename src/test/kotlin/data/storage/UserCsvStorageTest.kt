package data.storage

import com.google.common.truth.Truth.assertThat
import data.TestUtils
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class UserCsvStorageTest {

    private lateinit var storage: UserCsvStorage
    private lateinit var tempFilePath: String

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        tempFilePath = tempDir.resolve("users_test.csv").toString()
        storage = UserCsvStorage(tempFilePath)
    }

    @AfterEach
    fun tearDown() {
        TestUtils.cleanupFile(tempFilePath)
    }

    @Test
    fun `should create file with header when initialized`() {
        // WHEN - Storage is initialized in setUp

        // THEN - File should exist with header
        val content = java.io.File(tempFilePath).readText()
        assertThat(content).contains("id,username,password,type,createdAt")
    }

    @Test
    fun `should write and read users correctly`() {
        // GIVEN
        val user1 = createTestUser("user1", "password123", UserType.ADMIN)
        val user2 = createTestUser("user2", "secure456", UserType.MATE)
        val users = listOf(user1, user2)

        // WHEN
        storage.write(users)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)

        val resultUser1 = result.find { it.username == "user1" }
        assertThat(resultUser1).isNotNull()
        assertThat(resultUser1!!.password).isEqualTo("password123")
        assertThat(resultUser1.type).isEqualTo(UserType.ADMIN)

        val resultUser2 = result.find { it.username == "user2" }
        assertThat(resultUser2).isNotNull()
        assertThat(resultUser2!!.password).isEqualTo("secure456")
        assertThat(resultUser2.type).isEqualTo(UserType.MATE)
    }

    @Test
    fun `should append user to existing file`() {
        // GIVEN
        val user1 = createTestUser("user1", "password123", UserType.ADMIN)
        storage.write(listOf(user1))

        val user2 = createTestUser("user2", "secure456", UserType.MATE)

        // WHEN
        storage.append(user2)
        val result = storage.read()

        // THEN
        assertThat(result).hasSize(2)
        assertThat(result.map { it.username }).containsExactly("user1", "user2")
    }

    @Test
    fun `should handle empty list when reading`() {
        // GIVEN - Empty file with just header

        // WHEN
        val result = storage.read()

        // THEN
        assertThat(result).isEmpty()
    }

    private fun createTestUser(username: String, password: String, type: UserType): User {
        return User( username = username, password =  password, type = type)
    }
}