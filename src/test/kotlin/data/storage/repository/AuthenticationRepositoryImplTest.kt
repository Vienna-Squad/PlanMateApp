package data.storage.repository

import data.storage.UserCsvStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.data.storage.repository.AuthenticationRepositoryImpl
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthenticationRepositoryImplTest {
    private lateinit var repository: AuthenticationRepositoryImpl
    private lateinit var storage: UserCsvStorage

    private val user = User(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        username = "user1",
        hashedPassword = "pass1",
        type = UserType.ADMIN,
        cratedAt = LocalDateTime.now()
    )

    private val anotherUser = User(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
        username = "user2",
        hashedPassword = "pass2",
        type = UserType.MATE,
        cratedAt = LocalDateTime.now()
    )


    @BeforeEach
    fun setup() {
        storage = mockk(relaxed = true)
        repository = AuthenticationRepositoryImpl(storage)
    }

    @Test
    fun `should return list of users when getAllUsers is called`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getAllUsers()

        // Then
        assertEquals(listOf(user, anotherUser), result.getOrThrow())
    }

    @Test
    fun `should return failure when getAllUsers fails`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getAllUsers()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should create user successfully  when createUser is called`() {
        // Given
        every { storage.read() } returns emptyList()
        val expectedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())

        // When
         repository.createUser(user)

        // Then
        verify { storage.append(expectedUser) }
    }

    @Test
    fun `should return failure when createUser is called with existing user`() {
        // Given
        every { storage.read() } returns listOf(user)

        // When
        val result = repository.createUser(user)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when createUser fails`() {
        // Given
        every { storage.read() } returns emptyList()
        every { storage.append(any()) } throws NotFoundException("")

        // When
        val result = repository.createUser(user)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should update current user when creating a new user`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } returns listOf(user.copy(hashedPassword = user.hashedPassword.toMD5()))
        val expectedAnotherUser = anotherUser.copy(hashedPassword = anotherUser.hashedPassword.toMD5())
        repository.createUser(anotherUser)
        every { storage.read() } returns listOf(user.copy(hashedPassword = user.hashedPassword.toMD5()), expectedAnotherUser)
        // When
        val currentUserResult = repository.getCurrentUser()

        // Then
        assertTrue(currentUserResult.isSuccess)
    }

    @Test
    fun `should return current user when getCurrentUser is called`() {
        // Given
        every { storage.read() } returns emptyList()
        val expectedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())
        repository.createUser(user)
        every { storage.read() } returns listOf(expectedUser)
        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return failure when getCurrentUser fails with no current user set`() {
        // Given
        every { storage.read() } returns listOf(user)

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getCurrentUser fails to read`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when getCurrentUser fails to find user`() {
        // Given
        every { storage.read() } returns emptyList()
        repository.createUser(user)
        every { storage.read() } returns emptyList()

        // Then
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)

    }

    @Test
    fun `should return user when getUser is called with valid id from multiple users`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getUserByID(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))

        // Then
        assertTrue(result.isSuccess)
    }


    @Test
    fun `should return failure when getUser is called with invalid id`() {
        // Given
        every { storage.read() } returns listOf(user, anotherUser)

        // When
        val result = repository.getUserByID(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))

        // Then
        assertTrue(result.isFailure)
    }


    @Test
    fun `should return failure when getUser fails to read`() {
        // Given
        every { storage.read() } throws NotFoundException("")

        // When
        val result = repository.getUserByID(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        // Then
        assertTrue(result.isFailure)
    }



    @Test
    fun `should login successfully with correct credentials`() {
        // Given
        val encryptedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())
        every { storage.read() } returns listOf(encryptedUser)

        // When
        val result = repository.login(user.username, user.hashedPassword)

        // Then
        assertTrue(result.isSuccess)
    }
    @Test
    fun `should fail login with incorrect password`() {
        // Given
        val encryptedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())
        every { storage.read() } returns listOf(encryptedUser)

        // When
        val result = repository.login(user.username, "wrongPassword")

        // Then
        assertTrue(result.isFailure)
    }
    @Test
    fun `should fail login with non-existent username`() {
        // Given
        every { storage.read() } returns listOf(user.copy(hashedPassword = user.hashedPassword.toMD5()))

        // When
        val result = repository.login("nonExistingUser", "somePassword")

        // Then
        assertTrue(result.isFailure)
    }
    @Test
    fun `should logout successfully`() {
        // Given
        val encryptedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())
        every { storage.read() } returns listOf(encryptedUser)
        repository.login(user.username, user.hashedPassword)

        // When
        val result = repository.logout()

        // Then
        assertTrue(result.isSuccess)
    }
    @Test
    fun `should fail to get current user after logout`() {
        // Given
        val encryptedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())
        every { storage.read() } returns listOf(encryptedUser)
        repository.login(user.username, user.hashedPassword)
        repository.logout()

        // When
        val result = repository.getCurrentUser()

        // Then
        assertTrue(result.isFailure)
    }
  
    private fun String.toMD5(): String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}