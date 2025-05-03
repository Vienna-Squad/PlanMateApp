//package data.storage
//
//import com.google.common.truth.Truth.assertThat
//import org.example.domain.entity.User
//import org.example.domain.entity.UserType
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.io.TempDir
//import java.nio.file.Path
//import org.junit.jupiter.api.assertThrows
//import java.io.File
//import java.io.FileNotFoundException
//import java.time.LocalDateTime
//
//class UserCsvStorageTest {
//    private lateinit var tempFile: File
//    private lateinit var storage: UserCsvStorage
//
//    @BeforeEach
//    fun setUp(@TempDir tempDir: Path) {
//        tempFile = tempDir.resolve("users_test.csv").toFile()
//        storage = UserCsvStorage(tempFile)
//    }
//
//    @Test
//    fun `should create file with header when initialized`() {
//        // Given - initialization in setUp
//
//        // When - file creation happens in init block
//
//        // Then
//        assertThat(tempFile.exists()).isTrue()
//        assertThat(tempFile.readText()).contains("id,username,password,type,createdAt")
//    }
//
//    @Test
//    fun `should correctly serialize and append a user`() {
//        // Given
//        val user = User(
//            id = "user123",
//            username = "abdo",
//            hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99", // md5 hash of "password"
//            type = UserType.ADMIN,
//            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00")
//        )
//
//        // When
//        storage.append(user)
//
//        // Then
//        val users = storage.read()
//        assertThat(users).hasSize(1)
//
//        val savedUser = users[0]
//        assertThat(savedUser.id).isEqualTo("user123")
//        assertThat(savedUser.username).isEqualTo("abdo")
//        assertThat(savedUser.hashedPassword).isEqualTo("5f4dcc3b5aa765d61d8327deb882cf99")
//        assertThat(savedUser.type).isEqualTo(UserType.ADMIN)
//    }
//
//    @Test
//    fun `should handle multiple users`() {
//        // Given
//        val user1 = User(
//            id = "user1",
//            username = "admin",
//            hashedPassword = "21232f297a57a5a743894a0e4a801fc3", // md5 hash of "admin"
//            type = UserType.ADMIN,
//            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00")
//        )
//
//        val user2 = User(
//            id = "user2",
//            username = "mate",
//            hashedPassword = "4ac1b63dfd3c7c4fb3c3a68134bd0c97", // md5 hash of "mate"
//            type = UserType.MATE,
//            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00")
//        )
//
//        // When
//        storage.append(user1)
//        storage.append(user2)
//
//        // Then
//        val users = storage.read()
//        assertThat(users).hasSize(2)
//        assertThat(users.map { it.id }).containsExactly("user1", "user2")
//        assertThat(users.map { it.type }).containsExactly(UserType.ADMIN, UserType.MATE)
//    }
//
//    @Test
//    fun `should correctly write a list of users`() {
//        // Given
//        val user1 = User(
//            id = "user1",
//            username = "admin",
//            hashedPassword = "21232f297a57a5a743894a0e4a801fc3",
//            type = UserType.ADMIN,
//            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00")
//        )
//
//        val user2 = User(
//            id = "user2",
//            username = "mate",
//            hashedPassword = "4ac1b63dfd3c7c4fb3c3a68134bd0c97",
//            type = UserType.MATE,
//            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00")
//        )
//
//        // When
//        storage.write(listOf(user1, user2))
//
//        // Then
//        val users = storage.read()
//        assertThat(users).hasSize(2)
//        assertThat(users.map { it.username }).containsExactly("admin", "mate")
//    }
//
//    @Test
//    fun `should overwrite existing content when using write`() {
//        // Given
//        val user1 = User(
//            id = "user1",
//            username = "original",
//            hashedPassword = "original_hash",
//            type = UserType.ADMIN,
//            cratedAt = LocalDateTime.parse("2023-01-01T10:00:00")
//        )
//
//        val user2 = User(
//            id = "user2",
//            username = "new",
//            hashedPassword = "new_hash",
//            type = UserType.MATE,
//            cratedAt = LocalDateTime.parse("2023-01-02T10:00:00")
//        )
//
//        // First add user1
//        storage.append(user1)
//
//        // When - overwrite with user2
//        storage.write(listOf(user2))
//
//        // Then
//        val users = storage.read()
//        assertThat(users).hasSize(1)
//        assertThat(users[0].id).isEqualTo("user2")
//        assertThat(users[0].username).isEqualTo("new")
//    }
//
//    @Test
//    fun `should handle reading from non-existent file`() {
//        // Given
//        val nonExistentFile = File("non_existent_file.csv")
//        val invalidStorage = UserCsvStorage(nonExistentFile)
//
//        // Ensure the file doesn't exist before reading
//        if (nonExistentFile.exists()) {
//            nonExistentFile.delete()
//        }
//
//        // When/Then
//        assertThrows<FileNotFoundException> { invalidStorage.read() }
//
//        // Clean up
//        if (nonExistentFile.exists()) {
//            nonExistentFile.delete()
//        }
//    }
//
//    @Test
//    fun `should throw IllegalArgumentException when reading malformed CSV`() {
//        // Given
//        tempFile.writeText("id1,username1\n") // Missing columns
//
//        // When/Then
//        assertThrows<IllegalArgumentException> { storage.read() }
//    }
//
//    @Test
//    fun `should return empty list when file has only header`() {
//        // Given
//        // Only header is written during initialization
//
//        // When
//        val users = storage.read()
//
//        // Then
//        assertThat(users).isEmpty()
//    }
//}